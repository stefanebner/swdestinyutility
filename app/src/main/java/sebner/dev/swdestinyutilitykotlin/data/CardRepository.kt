package sebner.dev.swdestinyutilitykotlin.data

import android.arch.lifecycle.Observer
import sebner.dev.swdestinyutilitykotlin.data.database.CardsDao
import sebner.dev.swdestinyutilitykotlin.data.network.SwDestinyNetworkDataSource
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.utils.AppExecutors

/**
 * Handles data operations for all the cards.
 */
class CardRepository private constructor(
        private val dao:CardsDao,
        private val dataSource: SwDestinyNetworkDataSource,
        private val executors: AppExecutors
) {

    init {
        val networkData = dataSource.getActiveCards()
        val cardObserver = Observer<Array<Card>> { data -> executors.diskIO.execute { dao.insertAll(data) } }
        networkData.observeForever(cardObserver)
        checkIfFetchNeeded()
    }

    private var isInitialized = false

    companion object {

        @Volatile private var INSTANCE: CardRepository? = null

        fun getInstance(dao: CardsDao, dataSource: SwDestinyNetworkDataSource,
                        executors: AppExecutors) : CardRepository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: createCardRepository(dao, dataSource, executors).also { INSTANCE = it }
                }

        private fun createCardRepository(dao: CardsDao, dataSource: SwDestinyNetworkDataSource,
                                         executors: AppExecutors) = CardRepository(dao, dataSource, executors)
    }

    private fun startSync() {
        dataSource.startCardSync()
    }

    private fun doCardDataSync() {
        if (isInitialized) return else isInitialized = true

        dataSource.scheduleRecurringCardSync()
        executors.diskIO.execute { startSync() }
    }

    private fun checkIfFetchNeeded() {
        AppExecutors.getInstance().diskIO.execute({
            if (dao.getCardsWithMissingImages().isNotEmpty()) {
                doCardDataSync()
            }
        })

    }

    fun getAll() = dao.getAll()

    fun getAllCardsWithDice() = dao.getAllCardsWithDice()

    fun getCardsWith(searchTerm: String, filter: Set<String>) = dao.getCardsWith(searchTerm, filter)

    fun clearAll() = dao.clearAll()

    fun getCharacters() = dao.getCharacters()

    fun getCharactersWith(filter: Set<String>) = dao.getCharactersWith(filter)
}