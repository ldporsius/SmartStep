package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isPositive
import assertk.assertions.isZero
import org.junit.Test
import java.math.BigDecimal
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class WeightTest {

    val grams = GramsWeight(1000.0)
    val kilograms = KGWeight(1.0)

    @Test
    fun `baseFactor positive value validation`() {
        // Verify that implementations return a positive double value representing a valid conversion factor.
        val converted = convertWeight(grams, KG)
        assertThat(converted.weight ).isPositive()
    }

    @Test
    fun `baseFactor zero value handling`() {
        // Check how the system handles a baseFactor of 0.0, which might lead to division by zero in conversion logic.
        val testConverter = TestConverter(baseFactor = 0.0)
        try {
            val converted = convertWeight(grams, testConverter)
        }catch (e: Exception){
            assertThat(e).isInstanceOf(Exception::class)
            assertThat(e.message).isEqualTo("Cannot convert to zero")
        }
    }

    @Test
    fun `baseFactor negative value handling`() {
        // Verify if negative double values are permitted or if they trigger validation errors in the domain model.
        val negativeGrams = GramsWeight(-1000.0)
        val converted = convertWeight(negativeGrams, KG)
        assertThat(converted.weight.roundToInt()).isEqualTo(-1)
    }

    @Test
    fun `baseFactor NaN value check`() {
        // Test the behavior when input is Double.NaN to ensure it doesn't corrupt downstream weight calculations.
        val doubleNaNGrams = GramsWeight(Double.NaN)
        println("--- doubleNaNGrams: $doubleNaNGrams")
        val converted = convertWeight(doubleNaNGrams, KG)
        println("--- converted doubleNaNGrams to Kg: $converted")
        assertThat(converted.weight).isEqualTo(Double.NaN)
    }

    @Test
    fun `baseFactor infinity value check`() {
        // Test behavior with Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY to ensure mathematical stability.
       val positiveInfGrams = GramsWeight(Double.POSITIVE_INFINITY)
        val converted = convertWeight(positiveInfGrams, KG)
        assertThat(converted.weight.roundToInt()).isEqualTo( Int.MAX_VALUE )

        val negativeInfGrams = GramsWeight(Double.NEGATIVE_INFINITY)
        val converted2 = convertWeight(negativeInfGrams, KG)
        assertThat(converted2.weight.roundToInt()).isEqualTo( Int.MIN_VALUE )

    }

    @Test
    fun `baseFactor maximum double value`() {
        // Verify precision and behavior when baseFactor is set to Double.MAX_VALUE.
        //we are dividing by baseFactor, a very large number. we expect after rounding the result to be zero

        val testConverter = TestConverter(baseFactor = Double.MAX_VALUE)
        val converted = convertWeight(grams, testConverter)
        assertThat(converted.weight.roundToInt()).isZero()

        //we are dividing by the same value, expecting 1.0
        val tons = TestWeight(
            Double.MAX_VALUE,
            unit = GRAM
        )
        println("--- tons: ${tons.weight}, doubleMax: ${Double.MAX_VALUE}, tons div doubleMax: ${tons.weight / Double.MAX_VALUE}")
        val converted2 = convertWeight(tons, testConverter)
        assertThat(converted2.weight).isEqualTo(1.0)
    }

    @Test
    fun `baseFactor minimum positive value`() {
        // Verify precision and behavior when baseFactor is set to Double.MIN_VALUE (smallest positive non-zero value).
        println("--- Double.MIN_VALUE: ${Double.MIN_VALUE.toBigDecimal().toPlainString()}")
        val testConverter = TestConverter(baseFactor = Double.MIN_VALUE)
        val converted = convertWeight(grams, testConverter)
        assertThat(converted.weight).isEqualTo(Double.POSITIVE_INFINITY)
    }

    @Test
    fun `baseFactor implementation consistency`() {
        // Ensure that different implementations of the sealed interface (e.g., Grams, Kilograms) return the correct
        // expected hardcoded or calculated constant.
        assertThat(GRAM.baseFactor).isEqualTo(1.0)
        assertThat(KG.baseFactor).isEqualTo(1000.0)
        assertThat(LBS.baseFactor).isEqualTo(453.59237)

        val pounds = LBSWeight(100.0)
        val convertedPoundsToKg = convertWeight(pounds, KG)
        assertThat(convertedPoundsToKg.weight.roundToInt()).isEqualTo(45)

        val convertedPoundsToPounds = convertWeight(pounds, LBS)
        assertThat(convertedPoundsToPounds.weight.roundToInt()).isEqualTo(100)

        val convertedPoundsToGrams = convertWeight(pounds, GRAM)
        assertThat(convertedPoundsToGrams.weight.roundToInt()).isEqualTo(45359)

        //convert back
        val convertedGramsToPounds = convertWeight(convertedPoundsToGrams, LBS)
        assertThat(convertedGramsToPounds.weight.roundToInt()).isEqualTo(100)
        val convertedKgToPounds = convertWeight(convertedPoundsToKg, LBS)
        assertThat(convertedKgToPounds.weight.roundToInt()).isEqualTo(100)

    }

    @Test
    fun `baseFactor precision loss check`() {
        // Verify that baseFactor values with high decimal precision do not suffer from unexpected rounding errors
        // during retrieval.
        val pounds = LBSWeight(100.0)
        val convertedPoundsToKg = convertWeight(pounds, KG)
        assertThat(convertedPoundsToKg.weight.roundToInt()).isEqualTo(45)

        val convertedKgToPounds = convertWeight(convertedPoundsToKg, LBS)
        assertThat(convertedKgToPounds.weight.roundToInt()).isEqualTo(100)

        val Pounds200 = LBSWeight(200.0)
        val convertedToGrams = convertWeight(Pounds200, GRAM)

        assertThat(convertedToGrams.weight.roundToInt()).isEqualTo(90718)

        val convertedBackToPounds = convertWeight(convertedToGrams, LBS)
        assertThat(convertedBackToPounds.weight.roundToInt()).isEqualTo(200)
    }

}