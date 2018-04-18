package sebner.dev.swdestinyutilitykotlin.data.network

import android.app.IntentService
import android.content.Intent
import org.koin.android.ext.android.inject

/*
 * An IntentService to be fired immediately after start up to synchronize the currently
 * available sets. This should only be called when the application is on the screen.
 */
class SetSyncIntentService : IntentService("DestinySetSyncService") {
    private val networkDataSource by inject<SwDestinyNetworkDataSource>()
    override fun onHandleIntent(i: Intent?) {
        networkDataSource.syncSets()
    }

}