package sebner.dev.swdestinyutilitykotlin.data.network

import android.app.IntentService
import android.content.Intent
import sebner.dev.swdestinyutilitykotlin.utils.InjectorUtils

/*
 * An IntentService to be fired immediately after start up to synchronize the currently
 * available sets. This should only be called when the application is on the screen.
 */
class SetSyncIntentService : IntentService("DestinySetSyncService") {
    override fun onHandleIntent(i: Intent?) {
        InjectorUtils().provideSWDestinyNetworkDataSource(applicationContext).syncSets()
    }

}