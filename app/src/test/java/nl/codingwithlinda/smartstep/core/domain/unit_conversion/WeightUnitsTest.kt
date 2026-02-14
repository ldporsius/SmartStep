package nl.codingwithlinda.smartstep.core.domain.unit_conversion

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.weightRangeKg
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.weightRangePounds
import org.junit.Assert.*
import org.junit.Test

class WeightUnitsTest {

    val grams = WeightUnits.Grams(1000)
    val kg = WeightUnits.KG(1)
    val pounds = WeightUnits.LBS(200)

    @Test
    fun `convert from grams to kilograms`() {
        val converted = grams.convert<WeightUnits.KG>(Weights.KG)
        assertEquals(1, converted.kg)
    }

    @Test
    fun `convert from kg to grams`() {
        val converted = kg.convert<WeightUnits.Grams>(Weights.GRAMS)
        assertEquals(1000, converted.grams)
    }

    @Test
    fun `convert from kg to pounds`() {
        val converted = kg.convert<WeightUnits.LBS>(Weights.LBS)
        assertEquals(2, converted.pounds)
    }

    @Test
    fun `convert from pounds to kilograms`() {
        val converted = pounds.convert<WeightUnits.KG>(Weights.KG)
        assertEquals(90, converted.kg)
    }

    @Test
    fun `convert from pounds to grams`() {
        val converted = pounds.convert<WeightUnits.Grams>(Weights.GRAMS)
        assertEquals(90_719, converted.grams)
    }

    @Test
    fun `convert from grams to pounds`() {
        val converted = grams.convert<WeightUnits.LBS>(Weights.LBS)
        assertEquals(2, converted.pounds)
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