package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import nl.codingwithlinda.smartstep.features.steps_override_user.domain.model.DateYYYYMMDD
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

object DailyStepCountCreator {

    fun create(count: Int, date: Long = System.currentTimeMillis()): DailyStepCount{
        val isDateMillis = date.toString().length >= 13
        val dateSeconds = if(isDateMillis) date.MillisToDay() else date.seconds.inWholeDays
        return DailyStepCount(
            dayEpochSeconds = dateSeconds,
            stepCount = count
        )
    }


    fun getTodayAsSeconds(): Long{
        val today = System.currentTimeMillis()
        val date = today.MillisToDay()
        return date
    }

    fun toDateYYYYMMDD(dayEpochSeconds: Long): DateYYYYMMDD {
        val isInputMillis = dayEpochSeconds.toString().length >= 13
        val instant = when(isInputMillis) {
            true -> Instant.ofEpochMilli(dayEpochSeconds)
            false -> Instant.ofEpochSecond(dayEpochSeconds)
        }
        val local = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
       /* if (local.year !in 0 .. 3000) {
            //try if input was in milliseconds
            val instant = Instant.ofEpochMilli(dayEpochSeconds)
            val local = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())

            return DateYYYYMMDD(
                YYYY = local.year,
                MM = local.monthValue,
                DD = local.dayOfMonth
            )
        }*/
        return DateYYYYMMDD(
            YYYY = local.year,
            MM = local.monthValue,
            DD = local.dayOfMonth
        )


    }

    fun fromDateYYYYMMDD(dateYYYYMMDD: DateYYYYMMDD): Long {
        val local = LocalDate.of(dateYYYYMMDD.YYYY, dateYYYYMMDD.MM, dateYYYYMMDD.DD)
        return local.toEpochDay()
    }

    private fun Long.MillisToDay(): Long{
        val instant = Instant.ofEpochMilli(this)
        val day = instant.truncatedTo(ChronoUnit.DAYS)

        return day.epochSecond
    }
}