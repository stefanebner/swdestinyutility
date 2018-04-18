package sebner.dev.swdestinyutilitykotlin.data

import android.arch.lifecycle.Observer
import org.jetbrains.anko.doAsync
import sebner.dev.swdestinyutilitykotlin.data.database.SWDestinyDatabase
import sebner.dev.swdestinyutilitykotlin.data.network.SwDestinyNetworkDataSource
import sebner.dev.swdestinyutilitykotlin.model.Set

/**
 * Handles data operations for all the sets.
 */
class SetRepository (
        database: SWDestinyDatabase,
        private val dataSource: SwDestinyNetworkDataSource
) {

    private val dao = database.db.setsDao()

    init {
        val networkData = dataSource.getActiveSets()
        val observer = Observer<List<Set>> { sets -> doAsync { dao.insertAll(sets) } }
        networkData.observeForever(observer)
    }

    private val currentSets = 8
    private var isInitialized = false

    fun startInitialSync() {
        if (isInitialized) return else isInitialized = true
        checkIfSyncIsNeeded()
    }

    private fun checkIfSyncIsNeeded() {
        doAsync {
            val sets = dao.getCurrentSetsForSync()
            if (sets.size < currentSets) {
                dataSource.startSetSyncService()
            }
        }
    }

    fun clearAll() {
        dao.clearAll()
    }

    fun getCurrentSetsForSync(): List<Set> = dao.getCurrentSetsForSync()
}