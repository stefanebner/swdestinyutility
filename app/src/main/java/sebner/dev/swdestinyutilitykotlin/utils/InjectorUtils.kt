package sebner.dev.swdestinyutilitykotlin.utils

import android.content.Context
import sebner.dev.swdestinyutilitykotlin.data.CardRepository
import sebner.dev.swdestinyutilitykotlin.data.SetRepository
import sebner.dev.swdestinyutilitykotlin.data.database.SWDestinyDatabase
import sebner.dev.swdestinyutilitykotlin.data.network.SwDestinyNetworkDataSource
import sebner.dev.swdestinyutilitykotlin.ui.battlefield.BattlefieldViewModelFactory
import sebner.dev.swdestinyutilitykotlin.ui.damage.DamageViewModelFactory
import sebner.dev.swdestinyutilitykotlin.ui.cards.CardListViewModelFactory

/*
 * Provides static methods to inject various classes needed inside the project
 */
class InjectorUtils {

    fun provideSetRepository(context: Context): SetRepository {
        val database = SWDestinyDatabase.getInstance(context.applicationContext)
        val executors = AppExecutors.getInstance()
        val dataSource = SwDestinyNetworkDataSource.getInstance(
                context.applicationContext, executors)
        return SetRepository.getInstance(database.setsDao(), dataSource, executors)
    }

    fun provideCardRepository(context: Context): CardRepository {
        val database = SWDestinyDatabase.getInstance(context.applicationContext)
        val executors = AppExecutors.getInstance()
        val dataSource = SwDestinyNetworkDataSource.getInstance(
                context.applicationContext, executors)
        return CardRepository.getInstance(database.cardsDao(), dataSource, executors)
    }

    fun provideSWDestinyNetworkDataSource(context: Context): SwDestinyNetworkDataSource {
        provideSetRepository(context.applicationContext)
        val executors = AppExecutors.getInstance()
        return SwDestinyNetworkDataSource.getInstance(context.applicationContext, executors)
    }

    fun provideCardListViewModelFactory(applicationContext: Context) =
            CardListViewModelFactory(provideCardRepository(applicationContext))

    fun provideBattlefieldViewModelFactory(applicationContext: Context) =
            BattlefieldViewModelFactory(provideCardRepository(applicationContext))

    fun provideDamageViewModelFactory(applicationContext: Context) =
            DamageViewModelFactory(provideCardRepository(applicationContext))
}