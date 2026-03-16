package nl.codingwithlinda.unit_conversion.data.weight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isPositive
import org.junit.Test
import kotlin.math.roundToInt

class WeightTest {

    val grams = GramsWeight(1000.0)
    val kilograms = KGWeight(1.0)

    val converter = WeightUnitConverter
    @Test
    fun `baseFactor positive value validation`() {
        // Verify that implementations return a positive double value representing a valid conversion factor.
        val convertedKg = converter.toKg(grams)
        assertThat(convertedKg.weight).isPositive()
    }


    @Test
    fun `baseFactor negative value handling`() {
        // Verify if negative double values are permitted or if they trigger validation errors in the domain model.
        val negativeGrams = GramsWeight(-1000.0)
        val converted = converter.toKg(negativeGrams)
        assertThat(converted.weight.roundToInt()).isEqualTo(-1)
    }

    @Test
    fun `baseFactor NaN value check`() {
        // Test the behavior when input is Double.NaN to ensure it doesn't corrupt downstream weight calculations.
        val doubleNaNGrams = GramsWeight(Double.NaN)
        println("--- doubleNaNGrams: $doubleNaNGrams")
        val converted = converter.toKg(doubleNaNGrams)
        println("--- converted doubleNaNGrams to Kg: $converted")
        assertThat(converted.weight).isEqualTo(Double.NaN)
    }

    @Test
    fun `baseFactor infinity value check`() {
        // Test behavior with Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY to ensure mathematical stability.
       val positiveInfGrams = GramsWeight(Double.POSITIVE_INFINITY)
        val converted = converter.toKg(positiveInfGrams)
        assertThat(converted.weight.roundToInt()).isEqualTo( Int.MAX_VALUE )

        val negativeInfGrams = GramsWeight(Double.NEGATIVE_INFINITY)
        val converted2 = converter.toKg(negativeInfGrams)
        assertThat(converted2.weight.roundToInt()).isEqualTo( Int.MIN_VALUE )

    }

    @Test
    fun `baseFactor maximum double value`() {
        // Verify precision and behavior when baseFactor is set to Double.MAX_VALUE.
        //we are dividing by baseFactor, a very large number. we expect after rounding the result to be zero

    }


    @Test
    fun `baseFactor implementation consistency`() {
        // Ensure that different implementations of the sealed interface (e.g., Grams, Kilograms) return the correct
        // expected hardcoded or calculated constant.
        assertThat(Weight.GRAM.baseFactor).isEqualTo(1.0)
        assertThat(Weight.KG.baseFactor).isEqualTo(1000.0)
        assertThat(Weight.LBS.baseFactor).isEqualTo(453.59237)

        val pounds = LBSWeight(100.0)
        val convertedPoundsToKg = converter.toKg(pounds)
        assertThat(convertedPoundsToKg.weight.roundToInt()).isEqualTo(45)

        val convertedPoundsToPounds = converter.toLbs(pounds)
        assertThat(convertedPoundsToPounds.weight.roundToInt()).isEqualTo(100)

        val convertedPoundsToGrams = converter.toGram(pounds)
        assertThat(convertedPoundsToGrams.weight.roundToInt()).isEqualTo(45359)

        //convert back
        val convertedGramsToPounds = converter.toLbs(convertedPoundsToGrams)
        assertThat(convertedGramsToPounds.weight.roundToInt()).isEqualTo(100)
        val convertedKgToPounds = converter.toLbs(convertedPoundsToKg)
        assertThat(convertedKgToPounds.weight.roundToInt()).isEqualTo(100)

    }

    @Test
    fun `baseFactor precision loss check`() {
        // Verify that baseFactor values with high decimal precision do not suffer from unexpected rounding errors
        // during retrieval.
        val pounds = LBSWeight(100.0)
        val convertedPoundsToKg = converter.toKg(pounds)
        assertThat(convertedPoundsToKg.weight.roundToInt()).isEqualTo(45)

        val convertedKgToPounds = converter.toLbs(convertedPoundsToKg)
        assertThat(convertedKgToPounds.weight.roundToInt()).isEqualTo(100)

        val Pounds200 = LBSWeight(200.0)
        val convertedToGrams = converter.toGram(Pounds200)

        assertThat(convertedToGrams.weight.roundToInt()).isEqualTo(90718)

        val convertedBackToPounds = converter.toLbs(convertedToGrams)
        assertThat(convertedBackToPounds.weight.roundToInt()).isEqualTo(200)
    }

}