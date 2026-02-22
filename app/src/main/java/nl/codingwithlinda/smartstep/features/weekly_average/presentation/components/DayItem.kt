package nl.codingwithlinda.smartstep.features.weekly_average.presentation.components

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
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.weekly_average.presentation.model.DailyAverageUi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayItem(
    day: DailyAverageUi,
    modifier: Modifier = Modifier) {


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            progress = {
                day.average
            }
        )
        Text("${day.dateUi}")
        Text("${day.stepCount.stepCount}",
            style = MaterialTheme.typography.labelSmall)
    }
}

@Preview
@Composable
private fun PreviewDayItem() {
    val today = DateTimeHelper.toDateYYYYMMDD(System.currentTimeMillis())
    SmartStepTheme() {
        DayItem(
            day = DailyAverageUi(
                stepCount = DailyStepCount(
                    YYYY = today.YYYY,
                    MM = today.MM,
                    DD = today.DD,
                    2000),
            goal = DailyStepGoal(
                YYYY = today.YYYY,
                MM = today.MM,
                DD = today.DD
                ,1000),
            )
        )

    }
}