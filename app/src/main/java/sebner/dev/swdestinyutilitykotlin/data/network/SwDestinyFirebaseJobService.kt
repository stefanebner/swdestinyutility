package sebner.dev.swdestinyutilitykotlin.data.network

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import sebner.dev.swdestinyutilitykotlin.utils.InjectorUtils

/*
 * Firebase Job to periodically sync the cards
 * TODO only schedule job if cards unknown/pictures missing
 */
class SwDestinyFirebaseJobService : JobService() {
    override fun onStartJob(jobParam: JobParameters): Boolean {
        InjectorUtils().provideSWDestinyNetworkDataSource(applicationContext).startCardSync()
        jobFinished(jobParam, false)
        return true
    }

    override fun onStopJob(job: JobParameters) = true
}