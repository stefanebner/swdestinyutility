package sebner.dev.swdestinyutilitykotlin.data.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.firebase.jobdispatcher.*
import org.jetbrains.anko.startService
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.model.Set
import sebner.dev.swdestinyutilitykotlin.utils.AppExecutors
import sebner.dev.swdestinyutilitykotlin.utils.InjectorUtils
import sebner.dev.swdestinyutilitykotlin.utils.JsonParser
import java.util.concurrent.TimeUnit

/*
 * Provides the API to work with sets and cards
 */
class SwDestinyNetworkDataSource private constructor(
        val context: Context,
        private val appExecutors: AppExecutors
) {

    private val destinySets: MutableLiveData<Array<Set>> = MutableLiveData()
    private val allCards: MutableLiveData<Array<Card>> = MutableLiveData()
    private val syncIntervalHours: Int = 2
    private val syncIntervalSeconds: Int = TimeUnit.HOURS.toSeconds(syncIntervalHours.toLong()).toInt()
    private val syncFlextimeSeconds: Int = syncIntervalSeconds / 2
    private val swDestinySetSyncTag = "swdestiny-set-sync"

    companion object {

        @Volatile private var INSTANCE: SwDestinyNetworkDataSource? = null

        fun getInstance(context: Context, appExecutors: AppExecutors) : SwDestinyNetworkDataSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: createDataSource(
                            context, appExecutors).also { INSTANCE = it }
                }

        private fun createDataSource(context: Context, appExecutors: AppExecutors) =
                SwDestinyNetworkDataSource(context, appExecutors)

    }

    fun getActiveSets(): LiveData<Array<Set>> {
        return destinySets
    }

    fun getActiveCards(): LiveData<Array<Card>> {
        return allCards
    }

    fun startCardSync() = syncCards(InjectorUtils().provideSetRepository(context).getCurrentSetsForSync())

    fun startSetSyncService() {
        context.startService<SetSyncIntentService>()
    }

    fun syncSets() {
        appExecutors.networkIO.execute {
            try {
                val response:String? = NetworkUtils().getJsonResponse(NetworkUtils().getSetsUrl())
                val sets:Array<Set> = JsonParser().parseSetsFrom(response ?: "")
                destinySets.postValue(sets)
                syncCards(sets)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun syncCards(sets: Array<Set>) {
        appExecutors.networkIO.execute {
            try {
                sets
                        .asSequence()
                        .map { NetworkUtils().getJsonResponse(NetworkUtils().getCardsUrl(it.code)) }
                        .forEach { allCards.postValue(JsonParser().parseCardsFrom(it ?: "")) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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