package sebner.dev.swdestinyutilitykotlin.data.database

import android.arch.persistence.room.OnConflictStrategy
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import sebner.dev.swdestinyutilitykotlin.model.Card

/**
 * Database Access Object for the Cards database
 */
@Dao
interface CardsDao {
    @Query("SELECT * FROM cards")
    fun getAll(): LiveData<List<Card>>

    @Query("SELECT * FROM cards")
    fun getCurrentSets(): List<Card>

    @Query("SELECT * FROM cards WHERE affiliation_code NOT IN (:filter) AND " +
            "type_code NOT IN (:filter) AND faction_code NOT IN (:filter) " +
            "AND name LIKE '%' || :searchTerm || '%'")
    fun getCardsWith(searchTerm: String, filter: Set<String>): List<Card>

    @Query("SELECT * FROM cards WHERE type_code IS 'character'")
    fun getCharacters():  LiveData<List<Card>>

    @Query("SELECT * FROM cards WHERE affiliation_code NOT IN (:filter) AND " +
            "type_code IS 'character' AND " +
            "type_code NOT IN (:filter) AND faction_code NOT IN (:filter)")
    fun getCharactersWith(filter: Set<String>): List<Card>

    @Query("SELECT * FROM cards WHERE imagesrc IS ''")
    fun getCardsWithMissingImages(): List<Card>

    @Query("SELECT * FROM cards WHERE has_die IS 1")
    fun getAllCardsWithDice(): LiveData<List<Card>>

    @Query("DELETE FROM cards")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cards: Array<Card>?)
}