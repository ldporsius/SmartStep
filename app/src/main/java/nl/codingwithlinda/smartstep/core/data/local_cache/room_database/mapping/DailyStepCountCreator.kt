package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.features.steps.domain.model.DateYYYYMMDD
import nl.codingwithlinda.smartstep.features.steps.domain.model.years
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.toKotlinInstant

object DailyStepCountCreator {

    fun create(count: Int, date: Long = System.currentTimeMillis()): DailyStepCount{
        return DailyStepCount(
            date = date.toDate(),
            stepCount = count
        )
    }

    fun getTodaysCount(counts: List<DailyStepCount>, today: Long): DailyStepCount?{
        val day = today.toDate()

        return counts.lastOrNull {
            it.date == day
        }
    }

    fun getTodayAsSeconds(): Long{
        val today = System.currentTimeMillis()
        val date = today.toDate()
        return date
    }

    fun toDateYYYYMMDD(dateSeconds: Long): DateYYYYMMDD {
        val instant = Instant.ofEpochSecond(dateSeconds)
        val local = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        if (local.year !in years) {
            //try if input was in milliseconds
            val instant = Instant.ofEpochMilli(dateSeconds)
            val local = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
            if (local.year !in years) throw Exception("Invalid date")
            return DateYYYYMMDD(
                YYYY = local.year,
                MM = local.monthValue,
                DD = local.dayOfMonth
            )
        }
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

    private fun Long.toDate(): Long{
        val instant = Instant.ofEpochMilli(this)
        val day = instant.truncatedTo(ChronoUnit.DAYS)

        return day.epochSecond
    }
}