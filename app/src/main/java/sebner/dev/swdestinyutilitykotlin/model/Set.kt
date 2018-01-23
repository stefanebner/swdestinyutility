package sebner.dev.swdestinyutilitykotlin.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Defines the schema of a table in {@link Room} for a single set.
 * Is used to get all the available (@link Card) in a set.
 */
@Entity(tableName = "sets")
data class Set(var name: String, @PrimaryKey var code: String, var position: Int,
               var available: String, var known: Int, var total: Int, var url :String)