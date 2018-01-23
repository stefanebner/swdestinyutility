package sebner.dev.swdestinyutilitykotlin.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/*
 * Global executor pool for the various tasks needed
 */
class AppExecutors private constructor(
        val diskIO: Executor,
        val networkIO: Executor,
        val mainThread: Executor
){

    companion object {

        @Volatile private var INSTANCE: AppExecutors? = null

        fun getInstance() : AppExecutors =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: createAppExecutors().also { INSTANCE = it }
                }

        private fun createAppExecutors() = AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3), MainThreadExecutor())

    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(runnable: Runnable) {
            mainThreadHandler.post(runnable)
        }
    }
}