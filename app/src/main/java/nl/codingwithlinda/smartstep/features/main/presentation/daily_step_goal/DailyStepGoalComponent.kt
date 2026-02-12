package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.stepGoalRange

@Composable
fun DailyStepGoalComponent(
    selectedGoal: Int,
    onSelected: (goal: Int) -> Unit,
    onSave: (goal: Int) -> Unit,
    onDismiss: ()->Unit,
    modifier: Modifier = Modifier) {

            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {

                Text("Step Goal",
                    style = MaterialTheme.typography.titleMedium)

                DailyStepGoalPicker(
                    goals = stepGoalRange,
                    selectedGoal = selectedGoal,
                    onGoalSelected = {
                        onSelected(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 48.dp)

                )

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onSave(selectedGoal)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
                TextButton(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }

}