package sebner.dev.swdestinyutilitykotlin.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import sebner.dev.swdestinyutilitykotlin.model.Set

/**
 * Database Access Object for the Sets database
 */
@Dao
interface SetsDao {
    @Query("SELECT * FROM sets")
    fun getAll(): LiveData<List<Set>>

    @Query("SELECT * FROM sets")
    fun getCurrentSetsForSync(): List<Set>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sets: List<Set>?)

    @Update
    fun updateAll(vararg sets: Set)

    @Query("DELETE FROM sets")
    fun clearAll()
}