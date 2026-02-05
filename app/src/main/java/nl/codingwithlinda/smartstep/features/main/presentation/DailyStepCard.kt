package nl.codingwithlinda.smartstep.features.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design.ui.theme.primary
import nl.codingwithlinda.smartstep.design.ui.theme.secondary
import nl.codingwithlinda.smartstep.design.ui.theme.white
import java.util.Locale

@Composable
fun DailyStepCard(
    stepsTaken: Int,
    dailyGoal: Int,
    modifier: Modifier = Modifier) {

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
            Icon(painter = painterResource(R.drawable.sneakers), contentDescription = null)

            val fomattedSteps = String.format(Locale.getDefault(), "%,d", stepsTaken)

            Text(fomattedSteps,
                style = MaterialTheme.typography.headlineLarge)
            Text("/$dailyGoal Steps")

            LinearProgressIndicator(
                progress = {
                    stepsTaken.toFloat() / dailyGoal.toFloat()
                },
                modifier = Modifier
                    .height(12.dp)
                    ,
                color = white,
                trackColor = secondary,

                gapSize = 0.dp,
                drawStopIndicator = {}
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
            modifier = Modifier

        )
    }
}