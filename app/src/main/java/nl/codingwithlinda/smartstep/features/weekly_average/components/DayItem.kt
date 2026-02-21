package nl.codingwithlinda.smartstep.features.weekly_average.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import java.time.LocalDate

@Composable
fun DayItem(
    goal: DailyStepGoal,
    steps: DailyStepCount,
    modifier: Modifier = Modifier) {

    val weekday = remember {
        LocalDate.ofEpochDay(steps.dayEpochDay).dayOfWeek.getDisplayName(
            java.time.format.TextStyle.SHORT,
            java.util.Locale.getDefault()
        )
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            progress = {
                steps.stepCount.toFloat() / goal.goal
            }
        )
        Text("${weekday}")
        Text("${steps.stepCount}",
            style = MaterialTheme.typography.labelSmall)
    }
}

@Preview
@Composable
private fun PreviewDayItem() {
    val today = DailyStepCountCreator.getTodayAsYYYYMMDD()
    SmartStepTheme() {
        DayItem(
            goal = DailyStepGoal(today.dateEpochDay,1000),
            steps = DailyStepCount(
                YYYY = today.YYYY,
                MM = today.MM,
                DD = today.DD,
                2000)

        )
    }
}