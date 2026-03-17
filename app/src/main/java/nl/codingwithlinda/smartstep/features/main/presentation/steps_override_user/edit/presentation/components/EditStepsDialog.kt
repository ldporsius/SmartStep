package nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SettingBoxComponent

@Composable
fun EditStepsDialog(
    dateSelected: String,
    numSteps: Int,
    action: (EditStepAction) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier) {

    val numStepsText = remember(numSteps) {
        numSteps.toString()
    }
    Column(
        modifier = modifier,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        Text("Edit steps",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text("Calories, distance & duration will be recalculated accordingly")

        SettingBoxComponent(
            label = "Date",
            action = {
                //show date picker
                action(EditStepAction.ShowDatePicker)
            }
        ) {
            Text(dateSelected)
        }

        OutlinedTextField(
            value = numStepsText,
            onValueChange = {
                action(EditStepAction.SetSteps(it))
            },
            label = {
                Text("Steps")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        CommonDialogButtonRow(
            onDismiss = onDismiss,
            onSave = onSave
        )

    }
}