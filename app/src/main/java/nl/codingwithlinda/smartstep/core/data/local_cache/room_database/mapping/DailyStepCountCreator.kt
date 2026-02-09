package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import java.time.Instant
import java.time.temporal.ChronoUnit

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

    private fun Long.toDate(): Long{
        val instant = Instant.ofEpochMilli(this)
        val day = instant.truncatedTo(ChronoUnit.DAYS)

        return day.epochSecond
    }
}