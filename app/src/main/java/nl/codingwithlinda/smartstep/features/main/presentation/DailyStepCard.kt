package nl.codingwithlinda.smartstep.features.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.primary
import nl.codingwithlinda.smartstep.design_system.ui.theme.secondary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.smartstep.features.statistics.presentation.components.StatisticsItem
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi
import nl.codingwithlinda.smartstep.tests.fakeStatistics
import java.util.Locale

@Composable
fun DailyStepCard(
    stepsTaken: Int,
    dailyGoal: Int,
    statisticsUi: StatisticsUi,
    actionEdit: () -> Unit = {},
    actionPause: () -> Unit = {},
    modifier: Modifier = Modifier) {

    val iconModifier = remember {
        Modifier
            .background(color = white.copy(.5f), shape = CircleShape)
            .padding(8.dp)
    }
    val iconModifierSquare = remember {
        Modifier
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
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {

            Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
                Icon(painter = painterResource(R.drawable.pause),
                    contentDescription = "pause",
                    modifier = iconModifier
                        .then(
                            Modifier.clickable(){
                                actionPause()
                            }
                        )
                )
            }

            val fomattedSteps = String.format(Locale.getDefault(), "%,d", stepsTaken)

            Text(fomattedSteps,
                style = MaterialTheme.typography.headlineLarge)
            Text("/$dailyGoal Steps")

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
        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            StatisticsItem(
                icon = R.drawable.location_track,
                value = statisticsUi.distance,
                modifier = iconModifier.then(
                    Modifier.semantics(){
                        contentDescription = "distance"
                    }
                )
            )

            StatisticsItem(
                icon = R.drawable.weight_diet,
                value = statisticsUi.energy,
                modifier = iconModifier.then(
                    Modifier.semantics(){
                        contentDescription = "energy"
                    }
                )
            )
            StatisticsItem(
                icon = R.drawable.time_clock,
                value = statisticsUi.time,
                modifier = iconModifier.then(
                    Modifier.semantics(){
                        contentDescription = "time"
                    }
                )
            )

        }
    }

}

@Preview
@Composable
private fun PreviewDailyStepCard() {
    SmartStepTheme {
        DailyStepCard(
            stepsTaken = 1000,
            dailyGoal = 2000,
            statisticsUi = fakeStatistics,
            modifier = Modifier.width(480.dp)

        )
    }
}