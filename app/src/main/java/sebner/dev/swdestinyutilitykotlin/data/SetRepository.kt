package sebner.dev.swdestinyutilitykotlin.data

import android.arch.lifecycle.Observer
import sebner.dev.swdestinyutilitykotlin.data.database.SetsDao
import sebner.dev.swdestinyutilitykotlin.data.network.SwDestinyNetworkDataSource
import sebner.dev.swdestinyutilitykotlin.model.Set
import sebner.dev.swdestinyutilitykotlin.utils.AppExecutors

/**
 * Handles data operations for all the sets.
 */
class SetRepository private constructor(
        private val dao:SetsDao,
        private val dataSource: SwDestinyNetworkDataSource,
        private val executors: AppExecutors
) {

    init {
        val networkData = dataSource.getActiveSets()
        val observer = Observer<Array<Set>> { sets -> executors.diskIO.execute { dao.insertAll(sets) } }
        networkData.observeForever(observer)
    }

    private val currentSets = 6
    private var isInitialized = false

    companion object {

        @Volatile private var INSTANCE: SetRepository? = null

        fun getInstance(dao: SetsDao, dataSource: SwDestinyNetworkDataSource,
                        executors: AppExecutors) : SetRepository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: createSetRepository(dao, dataSource, executors).also { INSTANCE = it }
                }

        private fun createSetRepository(dao: SetsDao, dataSource: SwDestinyNetworkDataSource,
                                        executors: AppExecutors) = SetRepository(dao, dataSource, executors)

    }

    fun startInitialSync() {
        initializeData()
    }

    private fun startSync() {
        dataSource.startSetSyncService()
    }

    private fun initializeData() {
        if (isInitialized) return else isInitialized = true

        executors.diskIO.execute {
            if (isFetchNeeded()) {
                startSync()
            }
        }
    }

    private fun isFetchNeeded() = dao.getAll().size  < currentSets

    fun clearAll() {
        dao.clearAll()
    }

    fun getCurrentSetsForSync(): Array<Set> = dao.getCurrentSetsForSync()
}