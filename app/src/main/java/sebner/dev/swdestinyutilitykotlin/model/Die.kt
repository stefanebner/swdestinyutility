package sebner.dev.swdestinyutilitykotlin.model

/**
 * Defines the model of a Die with (@link DieSides)
 */
class Die(dieSides: String) {
    private val parsedSides: MutableList<DieSide> by lazy {
        dieSides.split(' ').map { convertToDieSide(it) }.toMutableList()
    }

    fun getDieSides(): List<DieSide> = parsedSides
    fun getDieSide(pos: Int) = parsedSides[pos]
    fun getAmountDieSides() = parsedSides.size
    fun hasDieSide(side: TYPE) = parsedSides.any { it.type == side }
    fun getDieSides(side: TYPE) = parsedSides.filter { s -> s.type == side  }

    fun getTypeFromSide(side: String) : TYPE {
        return when {
            side.contains(TYPE.SPECIAL.id) -> TYPE.SPECIAL
            side.contains(TYPE.RANGED.id) -> TYPE.RANGED
            side.contains(TYPE.MELEE.id) -> TYPE.MELEE
            side.contains(TYPE.INDIRECT.id) -> TYPE.INDIRECT
            side.contains(TYPE.DISRUPT.id) -> TYPE.DISRUPT
            side.contains(TYPE.DISCARD.id) -> TYPE.DISCARD
            side.contains(TYPE.RESOURCE.id) -> TYPE.RESOURCE
            side.contains(TYPE.SHIELD.id) -> TYPE.SHIELD
            else -> TYPE.EMPTY
        }
    }

    private fun convertToDieSide(side: String): DieSide {
        return when (getTypeFromSide(side)) {
            TYPE.EMPTY -> DieSide(TYPE.EMPTY)
            TYPE.SPECIAL -> DieSide(TYPE.SPECIAL)
            else -> {
                val modifier = side.startsWith('+')
                val value = if (modifier) side.substring(1..1).toInt() else side.substring(0..0).toInt()
                val resource = if (side[side.lastIndex].isDigit()) side.substring(side.lastIndex..side.lastIndex).toInt() else 0
                DieSide(getTypeFromSide(side), modifier, value, resource)
            }
        }
    }
}