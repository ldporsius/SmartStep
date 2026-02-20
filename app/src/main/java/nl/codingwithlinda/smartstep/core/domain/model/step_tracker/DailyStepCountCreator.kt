package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toKotlinInstant

object DailyStepCountCreator {

    fun create(count: Int, date: DateYYYYMMDD): DailyStepCount{
        return DailyStepCount(
            YYYY = date.YYYY,
            MM = date.MM,
            DD = date.DD,
            stepCount = count
        )
    }

    fun create(count: Int, date: Long = System.currentTimeMillis()): DailyStepCount{
        val isDateMillis = date.toString().length >= 13
       // println("${date} with lengt: ${date.toString().length} isDateMillis: $isDateMillis")
        val dateInDays = if(isDateMillis) date.milliseconds.inWholeDays else date.seconds.inWholeDays

       // println("dateInDays: ${dateInDays.days}")
        val localDate = LocalDate.ofEpochDay(dateInDays)

        //println("localDate: $localDate")
        return DailyStepCount(
            YYYY = localDate.year,
            MM = localDate.monthValue,
            DD = localDate.dayOfMonth,
            stepCount = count.coerceAtLeast(0)
        )
    }


    fun getTodayAsSeconds(): Long{
        val today = System.currentTimeMillis()
        val date = today.MillisToDay()
        return date
    }


    fun toDateYYYYMMDD(
        dayEpochSeconds: Long,

    ): DateYYYYMMDD {
        val isInputMillis = dayEpochSeconds.toString().length >= 13
        val day = when(isInputMillis) {
            true -> dayEpochSeconds.milliseconds.inWholeDays
            false -> dayEpochSeconds.seconds.inWholeDays
        }
        val local = LocalDate.ofEpochDay(day)

        return DateYYYYMMDD(
            YYYY = local.year,
            MM = local.monthValue,
            DD = local.dayOfMonth
        )
    }


    private fun Long.MillisToDay(): Long{
        val instant = Instant.ofEpochMilli(this)
        val day = instant.truncatedTo(ChronoUnit.DAYS)

        return day.epochSecond
    }
}