package sebner.dev.swdestinyutilitykotlin.data.database

//import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.content.Context

class SWDestinyDatabase(context: Context) {
    val db by lazy { Room.databaseBuilder(context,
            Database::class.java, "SWDestinyDatabase.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}

//@Database(entities = [(Set::class), (Card::class)], version = 1)
//abstract class SWDestinyDatabase : RoomDatabase() {
//
//    abstract fun setsDao(): SetsDao
//    abstract fun cardsDao(): CardsDao
//
//    companion object {
//        @Volatile private var INSTANCE: SWDestinyDatabase? = null
//
//        fun getInstance(context: Context?): SWDestinyDatabase =
//                INSTANCE ?: synchronized(this) {
//                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
//                }
//
//        private fun buildDatabase(context: Context?) =
//                if (context != null) {
//                    Room.databaseBuilder(context.applicationContext,
//                            SWDestinyDatabase::class.java, "SWDestinyDatabase.db").build()
//                } else {
//                    throw Throwable()
//                }
//    }
//}