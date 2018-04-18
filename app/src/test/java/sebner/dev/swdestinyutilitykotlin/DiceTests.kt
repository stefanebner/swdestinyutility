package sebner.dev.swdestinyutilitykotlin

import org.junit.Assert.assertTrue
import org.junit.Test
import sebner.dev.swdestinyutilitykotlin.model.Die
import sebner.dev.swdestinyutilitykotlin.model.DieSide
import sebner.dev.swdestinyutilitykotlin.model.TYPE
import sebner.dev.swdestinyutilitykotlin.utils.DiceCalculation
import sebner.dev.swdestinyutilitykotlin.utils.roundToTwoDecimals
import kotlin.math.pow

class DiceTests {

    private val CHANCE_FIVE_SIDES = 5.0 / 6.0

    @Test
    fun `given die has a specific side`() {
        //given
        val die = Die("2RD 3RD1 1F 1Sh Sp -")
        //when
        val result = die.hasDieSide(TYPE.RANGED)
        //then
        assertTrue(result)
    }

    @Test
    fun `get six sided die`() {
        //given
        val die = Die("2RD 3RD1 1F 1Sh Sp -")
        //when
        val result = die.getAmountDieSides()
        //then
        assertTrue(result  == 6)
    }

    @Test
    fun `roll damage`() {
        //given
        val list = ArrayList<Die>()
        list.add(Die("2RD 2RD 1MD 1MD 1RD 1MD"))
        //when
        val result = DiceCalculation(list).getDamageRoll()
        //then
        assertTrue(result >= 1)
    }

    @Test
    fun `calculate battlefield roll`() {
        //given
        val list = ArrayList<Die>()
        list.add(Die("2RD +2RD 2MD 2MD 2RD 2MD"))
        val calculation = DiceCalculation(list)
        //when
        val result = calculation.getBattlefieldRoll()
        //then
        assertTrue(result == 2)
    }

    @Test
    fun `chance to roll base side`() {
        //given
        val list = ArrayList<Die>()
        list.add(Die("2RD - - - - -"))
        list.add(Die("2RD - - - - -"))
        //when
        val result = DiceCalculation(list).getChanceForBaseSide(list, TYPE.RANGED)
        //then
        assertTrue(result == 1.0 / 3.0)
    }

    @Test
    fun `calculate percentage to re-roll at least value X`() {
        //given
        val list = mutableListOf<Die>()
        list.add(Die("2RD - - - - -"))
//        list.add(Die("+2RD - - - - -"))
        list.add(Die("2RD - - - - -"))
        //when
        val result = DiceCalculation(list).getProbability(2, TYPE.RANGED, 0)
        //then
        val expected = 1.0 - (CHANCE_FIVE_SIDES * CHANCE_FIVE_SIDES)
        assertTrue(expected.roundToTwoDecimals() == result.roundToTwoDecimals())
    }

    @Test
    fun `calculate percentage to guaranteed roll X`() {
        //given
        val list = mutableListOf<Die>()
        list.add(Die("2RD 2RD 2RD 2RD 2RD 2RD"))
        list.add(Die("+2RD - - - - -"))
        list.add(Die("2RD - - - - -"))
        //when
        val result = DiceCalculation(list).getProbability(2, TYPE.RANGED, 0)
        //then
        assertTrue(result == 1.0)
    }

    @Test
    fun `calculate percentage to re-roll at least value X with Y resources`() {
        //given
        val list = mutableListOf<Die>()
        list.add(Die("2RD - - - - -"))
        list.add(Die("+2RD/1 - - - - -"))
        list.add(Die("2RD - - - - -"))
        //when
        val result = DiceCalculation(list).getProbability(4, TYPE.RANGED, 1)
        //then
        val maxOutcomes = 6.0.pow(list.size)
        val expected = 1.0 /maxOutcomes +   // chance all three roll a "1"
                1.0 * 3.0 * 5 / maxOutcomes // how many sides per die can we roll *
                                            // number of ways to choose 2 dice *
                                            // how many sides can show on the remaining die
        assertTrue(expected.roundToTwoDecimals() == result.roundToTwoDecimals())
    }

    @Test
    fun `get best combination of pay sides`() {
        //given
        val list = mutableListOf<DieSide>()
        list.add(DieSide(TYPE.RANGED, true, 4, 3))
        list.add(DieSide(TYPE.RANGED, true, 2, 1))
        list.add(DieSide(TYPE.RANGED, true, 3, 2))
        list.add(DieSide(TYPE.RANGED, true, 2, 1))
        //when
        val result = DiceCalculation(listOf()).getBestPaySideCombination(list, 4)
        //then
        assertTrue(result == 7)
    }

    @Test
    fun `roll is valid to reach at least value X with Y resources`() {
        //given
        val list = mutableListOf<DieSide>()
        list.add(DieSide(TYPE.RANGED, false, 4, 0))
        list.add(DieSide(TYPE.RANGED, true, 1, 1))
        list.add(DieSide(TYPE.RANGED, true, 3, 2))
        list.add(DieSide(TYPE.RANGED, true, 2, 1))
        //when
        val result = DiceCalculation(listOf()).isValidRoll(TYPE.RANGED, list, 5, 1)
        //then
        assertTrue(result == 1)
    }
}