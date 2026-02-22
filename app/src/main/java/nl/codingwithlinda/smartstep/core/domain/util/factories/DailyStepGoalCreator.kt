package nl.codingwithlinda.smartstep.core.domain.util.factories

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import java.time.Instant
import java.time.temporal.ChronoUnit

object DailyStepGoalCreator {

    fun create(goal: Int, date: Long = System.currentTimeMillis()): DailyStepGoal {
        val localDate = DateTimeHelper.localDateFromMillis(date)

        return DailyStepGoal(
            YYYY = localDate.year,
            MM = localDate.monthValue,
            DD = localDate.dayOfMonth,
            goal = goal
        )
    }

    fun getTodaysGoal(goals: List<DailyStepGoal>, today: Long): DailyStepGoal?{

        val localDate = DateTimeHelper.toDateYYYYMMDD(today)


        return goals.lastOrNull {
            it.epochDay == localDate.dateEpochDay
        }
    }

}