package sebner.dev.swdestinyutilitykotlin.utils

import sebner.dev.swdestinyutilitykotlin.model.Die
import sebner.dev.swdestinyutilitykotlin.model.DieSide
import sebner.dev.swdestinyutilitykotlin.model.TYPE
import java.util.*
import kotlin.math.pow

class DiceCalculation(private val dice: List<Die>) {

    private val random = Random()
    private val SIDES_ON_A_DIE = 6.0

    fun getBattlefieldRoll(): Int {
        var result = 0
        dice.forEach({ die ->
            result = result.plus(die.getDieSide(random.nextInt(die.getAmountDieSides())).value)
        })
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

    fun getProbability(target: Int, type: TYPE, resources: Int) : Double {
        val validDice = dice.filter { d -> d.hasDieSide(type) }
        if (validDice.isEmpty()) return 0.0

        val all = cartesianProduct(validDice.map { it.getDieSides() })
        val validRolls = checkAllCombinations(type, all, target, resources)
        return validRolls / SIDES_ON_A_DIE.pow(validDice.size)
    }

    private fun flattenList(nestList: List<Any>): List<DieSide> {
        val flatList = mutableListOf<Any>()

        fun flatten(list: List<Any>) {
            for (e in list) {
                if (e !is List<*>) {
                    flatList.add(e)
                } else {
                    @Suppress("UNCHECKED_CAST")
                    flatten(e as List<Any>)
                }
            }
        }

        flatten(nestList)
        return flatList as List<DieSide>
    }

    operator fun List<Any>.times(other: List<Any>): List<List<Any>> {
        val prod = mutableListOf<List<Any>>()
        for (e in this) {
            for (f in other) {
                prod.add(listOf(e, f))
            }
        }
        return prod
    }

    private fun cartesianProduct(lists: List<List<DieSide>>): List<List<DieSide>> {
        require(lists.size >= 2)
        return lists.drop(2)
                .fold(lists[0] * lists[1]) { cp, ls -> cp * ls }
                .map { flattenList(it) }
    }

    private fun checkAllCombinations(type: TYPE, lists: List<List<DieSide>>, target: Int, res: Int) : Int {
        var validRolls = 0
        lists.forEach { roll ->
            validRolls += isValidRoll(type, roll, target, res)
        }
        return validRolls
    }

    fun isValidRoll(type: TYPE, roll: List<DieSide>, target: Int, res: Int) : Int {
        var valueAchieved = 0
        var baseRolled = false
        val paySides = mutableListOf<DieSide>()
        val modifiedSides = mutableListOf<DieSide>()

        roll.forEach { side ->
            if (side.type != type) return@forEach
            if (!side.isModifier) {
                baseRolled = true
                if (side.cost == 0) {
                    valueAchieved += side.value
                } else if (side.cost <= res) {
                    paySides.add(side)
                }
            } else {
                modifiedSides.add(side)
            }
        }

        if (baseRolled) {
            modifiedSides.forEach {side ->
                if (side.cost == 0) {
                    valueAchieved += side.value
                } else if (side.cost <= res) {
                    paySides.add(side)
                }

            }
        }
        if (res > 0) {
            valueAchieved += getBestPaySideCombination(paySides, res)
        }
        return if (valueAchieved >= target) 1 else 0
    }

    fun getBestPaySideCombination(sides: List<DieSide>, res: Int) : Int {
        if (sides.size == 1) return sides[0].value

        val sortedSides = sides.sortedWith(compareByDescending { it.value })
        var highestPaySides = 0

        sortedSides.forEachIndexed { index, side ->
            var currentValue = side.value
            var remainingResources = res - side.cost

            for (i in index + 1 until sortedSides.size) {
                if (remainingResources == 0) return@forEachIndexed
                if (sortedSides[i].cost <= remainingResources) {
                    currentValue += sortedSides[i].value
                    remainingResources -= sortedSides[i].cost
                }
            }

            if (currentValue > highestPaySides) highestPaySides = currentValue
        }

        return highestPaySides
    }

    fun getChanceForBaseSide(list: List<Die> = dice, type: TYPE) : Double {
        var chancesAdded = 0.0
        list.forEach {die ->
            die.getDieSides().forEach { side ->
                if (side.type == type && !side.isModifier) chancesAdded += 1.0/SIDES_ON_A_DIE
            }
        }
        return chancesAdded
    }
}
