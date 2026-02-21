package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import java.time.LocalDateTime
import java.time.Year
import java.time.YearMonth
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.milliseconds

class DailyStepCountCreatorTest {

    @Test
    fun `DailyStepCount creation with standard millisecond timestamp`() {
        // Verify that a 13-digit millisecond timestamp (e.g., System.currentTimeMillis()) 
        // is correctly converted to the expected YYYY, MM, and DD values.
        val today = System.currentTimeMillis()
        val expected = LocalDateTime.ofEpochSecond(today.milliseconds.inWholeSeconds, 0, ZoneOffset.UTC)
        val result = DailyStepCountCreator.create(100, today)

        val humanDate = "${expected.year}/${expected.monthValue}/${expected.dayOfMonth}"

        println("humanDate: $humanDate")

        assertThat(result.YYYY).isEqualTo(expected.year)


    }

    @Test
    fun `DailyStepCount creation with standard second timestamp`() {
        // Verify that a 10-digit second timestamp is correctly identified and 
        // converted to the expected YYYY, MM, and DD values.
        val today = System.currentTimeMillis()
        val expected = LocalDateTime.ofEpochSecond(today.milliseconds.inWholeSeconds, 0, ZoneOffset.UTC)
        val result = DailyStepCountCreator.create(100, today.milliseconds.inWholeSeconds)

        val humanDate = "${expected.year}/${expected.monthValue}/${expected.dayOfMonth}"

        println("humanDate: $humanDate")

        assertThat(result.YYYY).isEqualTo(expected.year)

    }

    @Test
    fun `Step count value integrity check`() {
        // Ensure the integer 'count' provided as an argument is accurately 
        // mapped to the 'stepCount' property of the resulting DailyStepCount object.
        val today = System.currentTimeMillis()
        val result = DailyStepCountCreator.create(100, today)

        assertThat(result.stepCount).isEqualTo(100)
    }

    @Test
    fun `Negative step count handling`() {
        // Test the behavior when count is negative to ensure the data 
        // class accepts or handles logical domain invariants for steps.
        val today = System.currentTimeMillis()
        val result = DailyStepCountCreator.create(-1, today)

        assertThat(result.stepCount).isEqualTo(0)
    }

    @Test
    fun `Zero step count handling`() {
        // Verify that a step count of zero is correctly assigned without 
        // any unexpected transformation.
        val today = System.currentTimeMillis()
        val result = DailyStepCountCreator.create(0, today)

        assertThat(result.stepCount).isEqualTo(0)
    }

    @Test
    fun `Maximum integer step count handling`() {
        // Test behavior when count is Int.MAX_VALUE to ensure no overflow 
        // or casting issues occur within the assignment.
        val today = System.currentTimeMillis()
        val result = DailyStepCountCreator.create(Int.MAX_VALUE, today)

        assertThat(result.stepCount).isEqualTo(Int.MAX_VALUE)
    }

    @Test
    fun `Timestamp boundary check for 12 vs 13 digits`() {
        // Verify the logic 'date.toString().length >= 13' by passing a 12-digit number 
        // to ensure it is treated as seconds, which is a critical edge case for the heuristic.
        val today = System.currentTimeMillis().toString().take(12).toLong()
        val expected = LocalDateTime.ofEpochSecond(today.milliseconds.inWholeSeconds, 0, ZoneOffset.UTC)
        val result = DailyStepCountCreator.create(100, today.milliseconds.inWholeSeconds)

        val humanDate = "${expected.year}/${expected.monthValue}/${expected.dayOfMonth}"

        println("humanDate: $humanDate")

        assertThat(result.YYYY).isEqualTo(expected.year)

    }

    @Test
    fun `Leap year date conversion`() {
        // Provide a timestamp representing February 29th on a leap year (e.g., 2024) 
        // to ensure the internal LocalDate conversion handles leap days correctly.
        val leapDate = YearMonth.of(2024, 2)
        val timestamp = leapDate.atDay(29).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val result = DailyStepCountCreator.create(100, timestamp)

        assertThat(result.YYYY).isEqualTo(2024)
        assertThat(result.MM).isEqualTo(2)
        assertThat(result.DD).isEqualTo(29)
    }

    @Test
    fun `Year boundary rollover check`() {
        // Provide a timestamp for December 31st vs January 1st to ensure 
        // year and month transitions are handled correctly by the conversion logic.
        val dec31 = YearMonth.of(2023, 12).atDay(31).atStartOfDay().plusHours(12).toEpochSecond(ZoneOffset.UTC)
        val jan1 = YearMonth.of(2024, 1).atDay(1).atStartOfDay().toEpochSecond(ZoneOffset.UTC)

        val resultDec31 = DailyStepCountCreator.create(100, dec31)
        val resultJan1 = DailyStepCountCreator.create(100, jan1)

        assertThat(resultDec31.YYYY).isEqualTo(2023)
        assertThat(resultDec31.MM).isEqualTo(12)
        assertThat(resultDec31.DD).isEqualTo(31)

        assertThat(resultJan1.YYYY).isEqualTo(2024)
        assertThat(resultJan1.MM).isEqualTo(1)
        assertThat(resultJan1.DD).isEqualTo(1)

    }

    @Test
    fun `Unix Epoch start date handling`() {
        // Pass a date value of 0 to verify the system handles the Unix Epoch 
        // (1970-01-01) correctly as a starting point.
        val result = DailyStepCountCreator.create(100, 0)

        assertThat(result.YYYY).isEqualTo(1970)
        assertThat(result.MM).isEqualTo(1)
        assertThat(result.DD).isEqualTo(1)
    }

    @Test
    fun `Millisecond to Day truncation verification`() {
        // Verify that the custom MillisToDay extension correctly truncates time 
        // components (hours/minutes/seconds) using ChronoUnit.DAYS via Instant.
        val todayAsSeconds = DailyStepCountCreator.getTodayAsYYYYMMDD()
        val expected = LocalDateTime.ofEpochSecond(System.currentTimeMillis().milliseconds.inWholeSeconds, 0, ZoneOffset.UTC)

        println("todayAsSeconds: $todayAsSeconds")
        println("expected: $expected")

        assertThat(expected.year).isEqualTo(todayAsSeconds.YYYY)
        assertThat(expected.monthValue).isEqualTo(todayAsSeconds.MM)
        assertThat(expected.dayOfMonth).isEqualTo(todayAsSeconds.DD)
    }

    @Test
    fun `Timezone dependency check for date creation`() {
        // Validate if the conversion to LocalDate via ofEpochDay (which uses UTC) 
        // aligns with the expected local date or if system timezone offsets cause drift.
        DailyStepCountCreator.toDateYYYYMMDD(System.currentTimeMillis()).also {
            val expected = LocalDateTime.ofEpochSecond(System.currentTimeMillis().milliseconds.inWholeSeconds, 0, ZoneOffset.UTC)
            assertThat(expected.year).isEqualTo(it.YYYY)
            assertThat(expected.monthValue).isEqualTo(it.MM)
            assertThat(expected.dayOfMonth).isEqualTo(it.DD)
        }
    }

    @Test
    fun `Extreme future date handling`() {
        // Test with a very large Long value to ensure LocalDate.ofEpochDay does 
        // not throw a DateTimeException for dates exceeding supported ranges.
        DailyStepCountCreator.toDateYYYYMMDD(System.currentTimeMillis() * 1000).also {
            val expected = LocalDateTime.ofEpochSecond(System.currentTimeMillis().times(1000).milliseconds.inWholeSeconds, 0, ZoneOffset.UTC)
            assertThat(expected.year).isEqualTo(it.YYYY)
            assertThat(expected.monthValue).isEqualTo(it.MM)
            assertThat(expected.dayOfMonth).isEqualTo(it.DD)
        }
    }

    @Test
    fun `Default parameter invocation check`() {
        // Call the create method without the date parameter to ensure 
        // System.currentTimeMillis() is used and produces a valid current DailyStepCount.
        DailyStepCountCreator.create(100).also {
            val expected = LocalDateTime.ofEpochSecond(System.currentTimeMillis().milliseconds.inWholeSeconds, 0, ZoneOffset.UTC)
            assertThat(expected.year).isEqualTo(it.YYYY)
            assertThat(expected.monthValue).isEqualTo(it.MM)
            assertThat(expected.dayOfMonth).isEqualTo(it.DD)
        }
    }

}