package nl.codingwithlinda.smartstep.core.domain.util.factories

import assertk.assertThat
import assertk.assertions.isBetween
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isNegative
import nl.codingwithlinda.smartstep.tests.util.testDate
import org.junit.Test
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.seconds

class DateTimeHelperTest {

    val helper = DateTimeHelper

    @Test
    fun `localDateFromMillis with 13 digit millisecond input`() {
        // Verify that a 13-digit timestamp (e.g., 1672531200000) is correctly identified as MILLIS 
        // and converted to the expected LocalDate (2023-01-01).
        val input = testDate.toEpochSecond(ZoneOffset.UTC).seconds.inWholeMilliseconds
        assertThat(input.toString().length).isGreaterThanOrEqualTo(13)
        val result = helper.toDateYYYYMMDD(input)
        assertThat(result.YYYY).isEqualTo(2026)
        assertThat(result.MM).isEqualTo(2)
        assertThat(result.DD).isEqualTo(21)
    }

    @Test
    fun `localDateFromMillis with 10 digit seconds input`() {
        // Verify that a 10-digit timestamp (e.g., 1672531200) is correctly identified as SECONDS 
        // and converted to the expected LocalDate (2023-01-01).
        val input = testDate.toEpochSecond(ZoneOffset.UTC).seconds.inWholeSeconds
        assertThat(input.toString().length).isBetween(10,12)
        val result = helper.toDateYYYYMMDD(input)
        assertThat(result.YYYY).isEqualTo(2026)
        assertThat(result.MM).isEqualTo(2)
        assertThat(result.DD).isEqualTo(21)
    }

    @Test
    fun `localDateFromMillis with small integer day input`() {
        // Verify that a small value (e.g., 19358) is correctly identified as DAYS 
        // and converted to the expected LocalDate (2023-01-01).
        val input = testDate.toEpochSecond(ZoneOffset.UTC).seconds.inWholeDays
        assertThat(input.toString().length).isBetween(5,9)
        val result = helper.toDateYYYYMMDD(input)
        assertThat(result.YYYY).isEqualTo(2026)
        assertThat(result.MM).isEqualTo(2)
        assertThat(result.DD).isEqualTo(21)
    }

    @Test
    fun `localDateFromMillis epoch zero handling`() {
        // Test with input 0 to ensure it is treated as 0 days and returns 1970-01-01.
        val input = 0L
        val result = helper.toDateYYYYMMDD(input)
        assertThat(result.YYYY).isEqualTo(1970)
        assertThat(result.MM).isEqualTo(1)
        assertThat(result.DD).isEqualTo(1)
    }

    @Test
    fun `localDateFromMillis negative value handling`() {
        // Test with negative Long values to verify behavior for dates before the Unix epoch 
        // and how length-based validation handles the '-' sign character.
        val input = -1L
        val result = helper.toDateYYYYMMDD(input)
        println("result from input -1: $result")
        assertThat(result.YYYY).isEqualTo(1969)
        assertThat(result.MM).isEqualTo(12)
        assertThat(result.DD).isEqualTo(31)
    }


    @Test
    fun `localDateFromMillis Long MAX VALUE safety`() {
        // Test with Long.MAX_VALUE to check for potential overflow in the Kotlin 
        // Duration conversion logic for milliseconds or seconds.
        val input = Long.MAX_VALUE
        val result = helper.toDateYYYYMMDD(input)
        println("result from input -1: $result")
        assertThat(result.YYYY).isNegative()

    }

}