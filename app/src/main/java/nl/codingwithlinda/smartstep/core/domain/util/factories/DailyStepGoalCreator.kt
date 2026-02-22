package nl.codingwithlinda.smartstep.core.domain.util.factories

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import java.time.Instant
import java.time.temporal.ChronoUnit

object DailyStepGoalCreator {

    fun create(goal: Int, date: Long = System.currentTimeMillis()): DailyStepGoal {
        val localDate = DateTimeHelper.toDateYYYYMMDD(date)

        return DailyStepGoal(
            YYYY = localDate.YYYY,
            MM = localDate.MM,
            DD = localDate.DD,
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