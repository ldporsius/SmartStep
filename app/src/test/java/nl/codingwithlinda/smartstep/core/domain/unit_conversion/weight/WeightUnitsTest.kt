package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.Weights
import org.junit.Assert
import org.junit.Test

class WeightUnitsTest {

    val grams = WeightUnits.Grams(1000)
    val kg = WeightUnits.KG(1)
    val pounds = WeightUnits.LBS(200)

    @Test
    fun `convert from grams to kilograms`() {
        val converted = grams.convert<WeightUnits.KG>(Weights.KG)
        Assert.assertEquals(1, converted.kg)
    }

    @Test
    fun `convert from kg to grams`() {
        val converted = kg.convert<WeightUnits.Grams>(Weights.GRAMS)
        Assert.assertEquals(1000, converted.grams)
    }

    @Test
    fun `convert from kg to pounds`() {
        val converted = kg.convert<WeightUnits.LBS>(Weights.LBS)
        Assert.assertEquals(2, converted.pounds)
    }

    @Test
    fun `convert from pounds to kilograms`() {
        val converted = pounds.convert<WeightUnits.KG>(Weights.KG)
        Assert.assertEquals(91, converted.kg)
    }

    @Test
    fun `convert from pounds to grams`() {
        val converted = pounds.convert<WeightUnits.Grams>(Weights.GRAMS)
        Assert.assertEquals(90_719, converted.grams)
    }

    @Test
    fun `convert from grams to pounds`() {
        val converted = grams.convert<WeightUnits.LBS>(Weights.LBS)
        Assert.assertEquals(2, converted.pounds)
    }


    @Test
    fun `print list of pounds and kg`() {
        val poundsKg = weightRangePounds.map {
            val pound = WeightUnits.LBS(it)
            val kg = pound.convert<WeightUnits.KG>(Weights.KG)

            it to kg.kg
        }

        poundsKg.groupBy { (pounds, kg) ->
            kg
        }.mapValues{ (i, pairs) ->
            println("$i kg == ")
            pairs.map { it.first }.onEach {p->
                println("--- $p pounds")
            }
        }
    }

}