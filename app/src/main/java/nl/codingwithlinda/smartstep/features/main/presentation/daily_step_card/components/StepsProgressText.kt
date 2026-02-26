package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import nl.codingwithlinda.smartstep.design_system.ui.theme.secondary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import java.util.Locale

@Composable
fun StepsProgressText(
    stepCount: Int,
    dailyGoal: Int,
    isPaused: Boolean,
    modifier: Modifier = Modifier) {

    val formattedSteps = String.format(Locale.getDefault(), "%,d", stepCount)

    val textColor = if (isPaused) white.copy(.5f) else white
    Column(
        modifier = modifier
    ) {
        Text(
            formattedSteps,
            style = MaterialTheme.typography.headlineLarge,
            color = textColor,
            modifier = Modifier.semantics(){
                contentDescription = "steps_taken"
            }
        )
        if (isPaused){
            Text("Paused", color = white)
        }
        else {
            Text("/$dailyGoal Steps",
                modifier = Modifier.semantics(){
                    contentDescription = "goal"
                })
        }
    }

}