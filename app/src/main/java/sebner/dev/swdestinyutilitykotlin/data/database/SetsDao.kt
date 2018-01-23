package sebner.dev.swdestinyutilitykotlin.data.database

import android.arch.persistence.room.*
import sebner.dev.swdestinyutilitykotlin.model.Set

/**
 * Database Access Object for the Sets database
 */
@Dao
interface SetsDao {
    @Query("SELECT * FROM sets")
    fun getAll(): List<Set>

    @Query("SELECT * FROM sets")
    fun getCurrentSetsForSync(): Array<Set>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sets: Array<Set>?)

    @Update
    fun updateAll(vararg sets: Set)

    @Query("DELETE FROM sets")
    fun clearAll()
}