package sebner.dev.swdestinyutilitykotlin.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Defines the schema of a table in {@link Room} for a single card
 * Also used to parse and create (@link gson) data from the website at the same time.
 */
@Entity(tableName = "cards")
class Card : Comparable<Card> {

    var die_sides: String = ""
    var set_code: String = ""
    var set_name: String = ""
    var type_code: String = ""
    var type_name: String = ""
    var faction_code: String = ""
    var faction_name: String = ""
    var affiliation_code: String = ""
    var affiliation_name: String = ""
    var rarity_code: String = ""
    var rarity_name: String = ""
    var position: Int = 0
    @PrimaryKey
    var code: String = ""
    var ttscardid: String = ""
    var name: String = ""
    var subtitle: String = ""
    var cost: Int = 0
    var health: Int = 0
    var points_single: Int = 0
    var points_elite: Int = 0
    var text: String = ""
    var deck_limit: Int = 0
    var flavor: String = ""
    var illustrator: String = ""
    var has_die: Boolean = false
    var has_errata: Boolean = false
    var unique: Boolean = false
    var url: String = ""
    var imagesrc: String = ""
    var label: String = ""
    var cp: Int = 0
    @Ignore
    var isElite = false

    @Ignore
    override fun compareTo(other: Card)= code.compareTo(other.code)

    @Ignore
    val defaultComparator: Comparator<Card> = Comparator { c1, c2 -> c1.code.compareTo(c2.code) }

    @Ignore
    val nameComparator: Comparator<Card> = Comparator { c1, c2 -> c1.name.compareTo(c2.name) }

    @Ignore
    val colorComparator: Comparator<Card> = object : Comparator<Card> {
        override fun compare(c1: Card, c2: Card): Int {
            // red, blue, yellow, neutral should be the order
            var ret = getColorInt(c2.faction_code) - getColorInt(c1.faction_code)
            if (ret == 0) ret = c1.name.compareTo(c2.name)
            return ret
        }

        private fun getColorInt(color: String): Int {
            return when (color) {
                "red" -> 5
                "blue" -> 4
                "yellow" -> 3
                "gray" -> 2
                else -> 1
            }
        }
    }

    @Ignore
    override fun toString() = name

    @Ignore
    fun getPoints(): String {
        return if (!unique) {
            points_single.toString()
        } else {
            points_elite.toString().plus(" / ").plus(points_single.toString())
        }
    }
}