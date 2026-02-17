package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.features.steps.domain.model.DateYYYYMMDD
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

    fun toDateYYYYMMDD(date: Long): DateYYYYMMDD {
        val instant = Instant.ofEpochSecond(date)
        val local = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        return DateYYYYMMDD(
            YYYY = local.year,
            MM = local.monthValue,
            DD = local.dayOfMonth
        )
    }

    private fun Long.toDate(): Long{
        val instant = Instant.ofEpochMilli(this)
        val day = instant.truncatedTo(ChronoUnit.DAYS)

        return day.epochSecond
    }
}