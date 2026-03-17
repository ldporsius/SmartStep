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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.secondary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components.PausePlayButton
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components.StatisticsRow
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components.StepsProgressText
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.interaction.DailyStepAction
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi

@Composable
fun DailyStepCard(
    stepsTaken: Int,
    dailyGoal: Int,
    statisticsUi: StatisticsUi,
    stepTrackerState: StepTrackerState,
    onAction: (DailyStepAction) -> Unit = {},
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
            .clickable(){
                onAction(DailyStepAction.ActionReport)
            }
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (progressText,
                    sneakerIcon,
                    editButton,
                    pauseButton,
                    reportButton) = createRefs()

                Icon(painter = painterResource(R.drawable.sneakers),
                    contentDescription = null,
                    modifier = iconModifierSquare
                        .constrainAs(sneakerIcon) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                )

                StepsProgressText(
                    stepCount = stepsTaken,
                    dailyGoal = dailyGoal,
                    isPaused = stepTrackerState == StepTrackerState.PAUSED,
                    modifier = Modifier.semantics {
                        contentDescription = "steps taken"
                    }
                        .constrainAs(progressText) {
                            top.linkTo(reportButton.baseline)
                            start.linkTo(parent.start)
                        }
                )
                IconButton(
                    onClick = {
                        onAction(DailyStepAction.ActionEdit)
                    },
                    modifier = iconModifier.then(
                        Modifier.constrainAs(editButton) {
                            top.linkTo(parent.top)
                            end.linkTo(pauseButton.start, margin = 8.dp)
                        }
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.pen_edit_2),
                        contentDescription = "edit steps",
                    )
                }
                PausePlayButton(
                    isPaused = stepTrackerState == StepTrackerState.PAUSED,
                    actionPause = {
                        onAction(DailyStepAction.ActionPause)
                    },
                    actionPlay = {
                        onAction(DailyStepAction.ActionPlay)
                    },
                    iconModifier = iconModifier.then(
                        Modifier.constrainAs(pauseButton) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }

                    )
                )

                TextButton(
                    onClick = {
                        onAction(DailyStepAction.ActionReport)
                    },
                    modifier = Modifier
                        .constrainAs(reportButton) {
                            top.linkTo(pauseButton.bottom, margin = 12.dp)
                            end.linkTo(parent.end)

                    }
                ) {
                    Text("Report >",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

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

@PreviewScreenSizes
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
            modifier = Modifier.width(480.dp)

        )
    }
}