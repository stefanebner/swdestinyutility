package sebner.dev.swdestinyutilitykotlin


import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import sebner.dev.swdestinyutilitykotlin.model.Die
import sebner.dev.swdestinyutilitykotlin.model.TYPE
import sebner.dev.swdestinyutilitykotlin.utils.DiceCalculation
import kotlin.collections.ArrayList

class DiceTests {

    @Test
    fun `given die has certain sides`() {
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
    fun `calculate damage`() {
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
        list.add(Die("2MD - - - - -"))
        list.add(Die("2RD - - - - -"))
        //when
        val result =DiceCalculation(list).getChanceForBaseSide(list, TYPE.RANGED)
        //then
        assertFalse(result == 1.0)
    }

    @Test
    fun `calculate percentage to reroll amount x`() {
        //given
        val list = ArrayList<Die>()
        list.add(Die("2RD 2RD 2RD 2RD 2RD 2RD"))
        list.add(Die("+2MD - - - - -"))
        list.add(Die("+2MD - - - - -"))
        //when
        val result = DiceCalculation(list).getProbability(2, TYPE.RANGED, 0)
        //then
        assertFalse(result == 1.0)
    }
}