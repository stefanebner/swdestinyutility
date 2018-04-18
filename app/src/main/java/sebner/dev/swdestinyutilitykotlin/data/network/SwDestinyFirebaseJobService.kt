package sebner.dev.swdestinyutilitykotlin.data.network

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import org.koin.android.ext.android.inject
import sebner.dev.swdestinyutilitykotlin.data.SetRepository

/*
 * Firebase Job to periodically sync the cards
 * TODO only schedule job if cards unknown/pictures missing
 */
class SwDestinyFirebaseJobService : JobService() {
    private val networkDataSource by inject<SwDestinyNetworkDataSource>()
    private val setsRepository by inject<SetRepository>()

    override fun onStartJob(jobParam: JobParameters): Boolean {
        networkDataSource.startCardSync(setsRepository.getCurrentSetsForSync())
        jobFinished(jobParam, false)
        return true
    }

    override fun onStopJob(job: JobParameters) = true
}