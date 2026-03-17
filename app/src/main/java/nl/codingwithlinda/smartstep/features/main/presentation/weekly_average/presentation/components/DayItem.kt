package nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation.model.DailyAverageUi

@Composable
fun DayItem(
    day: DailyAverageUi,
    textColor: Color = white,
    modifier: Modifier = Modifier) {


    CompositionLocalProvider(
        LocalTextStyle.provides(
            LocalTextStyle.current.copy(color = textColor)
        )
    ){
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                progress = {
                    day.average
                },
                color = Color.Green
            )
            Text("${day.dateUi}")
            Text(
                "${day.stepCount.stepCount}",
                style = MaterialTheme.typography.labelSmall,
                color = LocalTextStyle.current.color
            )
        }
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
                    200),
            goal = DailyStepGoal(
                YYYY = today.YYYY,
                MM = today.MM,
                DD = today.DD
                ,1000),
            )
        )

    }
}