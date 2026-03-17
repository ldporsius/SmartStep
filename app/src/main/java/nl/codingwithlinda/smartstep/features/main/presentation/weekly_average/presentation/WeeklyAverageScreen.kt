package nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.primary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation.components.DayItem
import nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation.model.DailyAverageUi
import nl.codingwithlinda.smartstep.util.fakeGoals
import nl.codingwithlinda.smartstep.util.fakeSteps

@Composable
fun WeeklyAverageScreen(
    days: List<DailyAverageUi>,
    modifier: Modifier = Modifier) {

    val bgModifier = Modifier
        .background(color = primary, shape = RoundedCornerShape(16.dp))
        .padding(16.dp)

    CompositionLocalProvider( LocalTextStyle.provides(
        LocalTextStyle.current.copy(color = white)
    )
    ) {
        LazyRow(
            modifier = modifier
                .then(bgModifier),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(days) { dayUi ->
                DayItem(dayUi)
            }
        }
    }
}


@Preview
@Composable
private fun PreviewWeeklyAverageScreen() {
    val days =  List(5){
        DailyAverageUi(
            stepCount = fakeSteps.get(it),
            goal = fakeGoals.get(it)
        )
    }


    SmartStepTheme() {
        WeeklyAverageScreen(
            days = days,
            modifier = Modifier.fillMaxWidth()
        )
    }
}