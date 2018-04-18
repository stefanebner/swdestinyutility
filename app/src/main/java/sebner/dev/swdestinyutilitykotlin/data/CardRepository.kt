package sebner.dev.swdestinyutilitykotlin.data

import android.arch.lifecycle.Observer
import org.jetbrains.anko.doAsync
import sebner.dev.swdestinyutilitykotlin.data.database.CardsDao
import sebner.dev.swdestinyutilitykotlin.data.database.SWDestinyDatabase
import sebner.dev.swdestinyutilitykotlin.data.database.SetsDao
import sebner.dev.swdestinyutilitykotlin.data.network.SwDestinyNetworkDataSource
import sebner.dev.swdestinyutilitykotlin.model.Card

/**
 * Handles data operations for all the cards.
 */
class CardRepository (
        database: SWDestinyDatabase,
        private val dataSource: SwDestinyNetworkDataSource
) {
    private val cardsDao: CardsDao = database.db.cardsDao()
    private val setsDao: SetsDao = database.db.setsDao()

    init {
        val networkData = dataSource.getActiveCards()
        val cardObserver = Observer<List<Card>> { data -> doAsync { cardsDao.insertAll(data) } }
        networkData.observeForever(cardObserver)
//        checkIfFetchNeeded()
    }

    private var isInitialized = false

    private fun startSync() {
        dataSource.startCardSync(setsDao.getCurrentSetsForSync())
    }

    private fun doCardDataSync() {
        if (isInitialized) return else isInitialized = true

        dataSource.scheduleRecurringCardSync()
        doAsync { startSync() }
    }

    private fun checkIfFetchNeeded() {
        if (cardsDao.getCardsWithMissingImages().isNotEmpty()) {
            doCardDataSync()
        }
    }

    fun getAll() = cardsDao.getAll()

    fun getAllCardsWithDice() = cardsDao.getAllCardsWithDice()

    fun getCardsWith(searchTerm: String, filter: Set<String>) = cardsDao.getCardsWith(searchTerm, filter)

    fun clearAll() = cardsDao.clearAll()

    fun getCharacters() = cardsDao.getCharacters()

    fun getCharactersWith(filter: Set<String>) = cardsDao.getCharactersWith(filter)
}