package sebner.dev.swdestinyutilitykotlin.data.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.Trigger
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startService
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.model.Set
import java.util.concurrent.TimeUnit

/*
 * Provides the API to work with sets and cards
 */
class SwDestinyNetworkDataSource(
        private val context: Context,
        private val restApi: RestApi
) {
    private val destinySets: MutableLiveData<List<Set>> = MutableLiveData()
    private val allCards: MutableLiveData<List<Card>> = MutableLiveData()
    private val syncIntervalHours: Int = 2
    private val syncIntervalSeconds: Int = TimeUnit.HOURS.toSeconds(syncIntervalHours.toLong()).toInt()
    private val syncFlextimeSeconds: Int = syncIntervalSeconds / 2
    private val swDestinySetSyncTag = "swdestiny-set-sync"

    fun getActiveSets(): LiveData<List<Set>> {
        return destinySets
    }

    fun getActiveCards(): LiveData<List<Card>> {
        return allCards
    }

    fun startCardSync(setsForSync: List<Set>) = syncCards(setsForSync)

    fun startSetSyncService() {
        context.startService<SetSyncIntentService>()
    }

    fun syncSets() {
        launch(CommonPool) {
            val sets = restApi.getSets()
            destinySets.postValue(sets.await())
            syncCards(sets.await())
        }
    }

    private fun syncCards(sets: List<Set>) {
        launch(CommonPool) {
            sets.forEach {
                val cards = getCards(it.code)
                allCards.postValue(cards)
            }
        }

//        try {
//            sets
//                .asSequence()
//                .map { it.code }
//                .forEach { launch(CommonPool) { allCards.postValue(getCards(it)) }}
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    private suspend fun getCards(set: String) = restApi.getCards(set).await()

    fun scheduleRecurringCardSync() {
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
        val job = dispatcher.newJobBuilder()
                .setService(SwDestinyFirebaseJobService::class.java)
                .setTag(swDestinySetSyncTag)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(
                        syncIntervalSeconds, syncIntervalSeconds + syncFlextimeSeconds))
                .build()
        dispatcher.schedule(job)
    }
}