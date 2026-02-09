package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import java.time.Instant
import java.time.temporal.ChronoUnit

object DailyStepGoalCreator {

    fun create(goal: Int, date: Long = System.currentTimeMillis()): DailyStepGoal{
        return DailyStepGoal(
            date = date.toDate(),
            goal = goal
        )

    }

    fun getTodaysGoal(goals: List<DailyStepGoal>, today: Long): DailyStepGoal?{
        val day = today.toDate()

        return goals.lastOrNull {
            it.date == day
        }
    }

    private fun Long.toDate(): Long{
        val instant = Instant.ofEpochMilli(this)
        val day = instant.truncatedTo(ChronoUnit.DAYS)

        return day.epochSecond
    }
}