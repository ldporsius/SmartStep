package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import org.junit.Assert
import org.junit.Test
import kotlin.math.roundToInt

class WeightUnitsTest {

    val grams = GramsWeight(1000.0)
    val kg = KGWeight(1.0)
    val pounds = LBSWeight(200.0)

    @Test
    fun `convert from grams to kilograms`() {
        val converted = convertWeight(grams, KG)
        Assert.assertEquals(1, converted.weight.roundToInt())
    }

    @Test
    fun `convert from kg to grams`() {
        val converted = convertWeight(kg, GRAM)
        Assert.assertEquals(1000, converted.weight.roundToInt())
    }

    @Test
    fun `convert from kg to pounds`() {
        val converted = convertWeight(kg, LBS)
        Assert.assertEquals(2, converted.weight.roundToInt())
    }

    @Test
    fun `convert from pounds to kilograms`() {
        val converted = convertWeight(pounds, KG)
        Assert.assertEquals(91, converted.weight.roundToInt())
    }

    @Test
    fun `convert from pounds to grams`() {
        val converted = convertWeight(pounds, GRAM)
        Assert.assertEquals(90_718, converted.weight.roundToInt())
    }

    @Test
    fun `convert from grams to pounds`() {
        val converted = convertWeight(grams, LBS)
        Assert.assertEquals(2, converted.weight.roundToInt())
    }




}