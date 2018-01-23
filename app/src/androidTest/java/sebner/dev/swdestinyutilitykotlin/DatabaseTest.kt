package sebner.dev.swdestinyutilitykotlin

import org.amshove.kluent.*
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import org.junit.runner.RunWith
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import sebner.dev.swdestinyutilitykotlin.data.database.CardsDao
import sebner.dev.swdestinyutilitykotlin.data.database.SWDestinyDatabase
import sebner.dev.swdestinyutilitykotlin.data.database.SetsDao
import sebner.dev.swdestinyutilitykotlin.model.Set
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var swDestinyDatabase: SWDestinyDatabase
    private lateinit var cardsDao: CardsDao
    private lateinit var setsDao: SetsDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        swDestinyDatabase = Room.inMemoryDatabaseBuilder(context, SWDestinyDatabase::class.java).build()
        cardsDao = swDestinyDatabase.cardsDao()
        setsDao = swDestinyDatabase.setsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        swDestinyDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun `test sets database`() {
        val set = Set("TestSet", "test", 1, "1", 1, 1, "noUrl")
        setsDao.insertAll(Array(1, { set }))
        val ret = setsDao.getAll()
        ret.size shouldEqualTo 1
    }

    @Test
    @Throws(Exception::class)
    fun `test cards database`() {

    }
}