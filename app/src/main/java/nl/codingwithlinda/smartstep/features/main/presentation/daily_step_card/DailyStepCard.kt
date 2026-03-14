package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.secondary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components.PausePlayButton
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components.StatisticsRow
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components.StepsProgressText
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi

@Composable
fun DailyStepCard(
    stepsTaken: Int,
    dailyGoal: Int,
    statisticsUi: StatisticsUi,
    stepTrackerState: StepTrackerState,
    actionEdit: () -> Unit = {},
    actionPause: () -> Unit ,
    actionPlay: () -> Unit,
    modifier: Modifier = Modifier) {

    val iconModifier = remember {
        Modifier
            .size(48.dp)
            .background(color = white.copy(.5f), shape = CircleShape)
            .padding(8.dp)
    }
    val iconModifierSquare = remember {
        Modifier
            .size(48.dp)
            .background(color = white.copy(.5f), shape = RoundedCornerShape(4.dp))
            .padding(8.dp)
    }
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(R.drawable.sneakers),
                    contentDescription = null,
                    modifier = iconModifierSquare)
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.pen_edit_2),
                    contentDescription = "edit steps",
                    modifier = iconModifier.then(
                        Modifier.clickable(){
                            actionEdit()
                        }
                    )

                )
                PausePlayButton(
                    isPaused = stepTrackerState == StepTrackerState.PAUSED,
                    actionPause = actionPause,
                    actionPlay = actionPlay,
                    iconModifier = iconModifier
                )
            }

            StepsProgressText(
                stepCount = stepsTaken,
                dailyGoal = dailyGoal,
                isPaused = stepTrackerState == StepTrackerState.PAUSED,
                modifier = Modifier.semantics {
                    contentDescription = "steps taken"
                }
            )

            LinearProgressIndicator(
                progress = {
                    stepsTaken.toFloat() / dailyGoal.toFloat()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    ,
                color = white,
                trackColor = secondary,

                gapSize = 0.dp,
                drawStopIndicator = {}
            )
        }

        //statistics
       StatisticsRow(
           statisticsUi = statisticsUi,
           iconModifier = iconModifierSquare
       )
    }

}

@Preview
@Composable
private fun PreviewDailyStepCard() {
    val dummyStatistics = StatisticsUi(
        distance = UiText.DynamicText("0"),
        energy = UiText.DynamicText("0"),
        time = UiText.DynamicText("0")
    )
    SmartStepTheme {
        DailyStepCard(
            stepsTaken = 1000,
            dailyGoal = 2000,
            stepTrackerState = StepTrackerState.STOPPED,
            statisticsUi =dummyStatistics,
            actionEdit = {},
            actionPause = {},
            actionPlay = {},
            modifier = Modifier.width(480.dp)

        )
    }
}