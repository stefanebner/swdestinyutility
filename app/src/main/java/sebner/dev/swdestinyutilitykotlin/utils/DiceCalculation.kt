package sebner.dev.swdestinyutilitykotlin.utils

import sebner.dev.swdestinyutilitykotlin.model.Die
import sebner.dev.swdestinyutilitykotlin.model.DieSide
import sebner.dev.swdestinyutilitykotlin.model.TYPE
import java.util.Random

class DiceCalculation(private val dice: List<Die>) {

    private val random = Random()

    fun getBattlefieldRoll(): Int {
        var result = 0
        dice.forEach({ die -> result = result.plus(die.getDieSide(die.getAmountDieSides()).value) })
        return result
    }

    fun getDamageRoll(): Int {
        val allDieSidesRolled = mutableListOf<DieSide>()
        var meleeBaseRolled = false
        var rangeBaseRolled = false
        var indirectBaseRolled = false
        var total = 0

        dice.mapTo(allDieSidesRolled) {
            val roll = it.getDieSide(random.nextInt(it.getAmountDieSides()))
            when(roll.type) {
                TYPE.RANGED -> if (!roll.isModifier) rangeBaseRolled = true
                TYPE.MELEE -> if (!roll.isModifier) meleeBaseRolled = true
                TYPE.INDIRECT -> if (!roll.isModifier) indirectBaseRolled = true
                else -> Unit
            }
            roll
        }

        allDieSidesRolled.forEach { side ->
            when(side.type) {
                TYPE.RANGED -> total += (if ((side.isModifier && rangeBaseRolled) || !side.isModifier) side.value else 0)
                TYPE.MELEE -> total += (if ((side.isModifier && meleeBaseRolled) || !side.isModifier) side.value else 0)
                TYPE.INDIRECT -> total += (if ((side.isModifier && indirectBaseRolled) || !side.isModifier) side.value else 0)
                else -> Unit
            }
        }
        return total
    }
}
