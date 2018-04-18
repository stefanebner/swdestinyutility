package sebner.dev.swdestinyutilitykotlin.model

/**
 * Data Class for a die side accompanying a (@link Card) with all possible types of die sides
 */
data class DieSide(val type: TYPE = TYPE.EMPTY, val isModifier: Boolean = false, val value: Int = 0, val cost: Int = 0)

enum class TYPE(val id: String = "-") {
    SPECIAL("Sp"),
    RANGED("RD"),
    MELEE("MD"),
    INDIRECT("ID"),
    EMPTY("-"),
    DISRUPT("Dr"),
    DISCARD("Dc"),
    RESOURCE("R"),
    SHIELD("SH")
}