package nl.codingwithlinda.smartstep.features.daily_step_goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.stepGoalRange
import nl.codingwithlinda.smartstep.design_system.components.CommonNumberPicker

@Composable
fun DailyStepGoalComponent(
    onDismiss: ()-> Unit,
    modifier: Modifier = Modifier) {


    val dailyStepGoalViewModel = viewModel<DailyStepGoalViewModel>(
        factory = viewModelFactory {
            initializer {
                DailyStepGoalViewModel(
                    appScope = SmartStepApplication.applicationScope,
                    dailyStepRepo = SmartStepApplication.dailyStepRepo
                )
            }
        }
    )

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Step Goal",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 48.dp,bottom = 16.dp)
        )

        CommonNumberPicker(
            label = "",
            values = stepGoalRange,
            selectedGoal = dailyStepGoalViewModel.goal.collectAsStateWithLifecycle().value,
            onGoalSelected = {
                dailyStepGoalViewModel.setGoal(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 48.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                val selectedGoal = dailyStepGoalViewModel.goal.value
                dailyStepGoalViewModel.saveGoal(selectedGoal)
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
        TextButton(
            onClick = {
                dailyStepGoalViewModel.dismissChanges()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }

}