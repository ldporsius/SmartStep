package nl.codingwithlinda.smartstep.features.weekly_average.presentation.model

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

data class DailyAverageUi(
    val stepCount: DailyStepCount,
    val goal: DailyStepGoal
){
    val date = stepCount.dayEpochDay
    val dateUi = YearMonth.of(stepCount.YYYY, stepCount.MM).atDay(stepCount.DD).dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.getDefault()
    )
    val average = stepCount.stepCount.toFloat() / goal.goal
}
