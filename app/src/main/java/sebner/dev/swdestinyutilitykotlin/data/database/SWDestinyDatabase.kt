package sebner.dev.swdestinyutilitykotlin.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.model.Set

/**
 * Singleton database object. Will contain two different tables for now - Cards and Sets
 */
@Database(entities = [(Set::class), (Card::class)], version = 1)
abstract class SWDestinyDatabase : RoomDatabase() {

    abstract fun setsDao(): SetsDao
    abstract fun cardsDao(): CardsDao

    companion object {
        @Volatile private var INSTANCE: SWDestinyDatabase? = null

        fun getInstance(context: Context?): SWDestinyDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context?) =
                if (context != null) {
                    Room.databaseBuilder(context.applicationContext,
                            SWDestinyDatabase::class.java, "SWDestinyDatabase.db").build()
                } else {
                    throw Throwable()
                }
    }
}