package nl.codingwithlinda.smartstep.features.steps.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SettingBoxComponent
import nl.codingwithlinda.smartstep.features.steps.presentation.state.EditStepAction

@Composable
fun EditStepsDialog(
    dateSelected: String,
    numSteps: Int,
    action: (EditStepAction) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text("Edit steps",
            style = MaterialTheme.typography.titleLarge,)
        Text("Calories, distance & duration will be recalculated accordingly”")

        SettingBoxComponent(
            label = "Date",
            action = {
                //show date picker
            }
        ) {
            Text(dateSelected)
        }

        OutlinedTextField(
            value = numSteps.toString(),
            onValueChange = {
                action(EditStepAction.SetSteps(it))
            },
            label = {
                Text("Steps")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
            ,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
        ) {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(12.dp))
            TextButton( onClick = {
                onSave()
            }
            ) {
                Text("Save")
            }
        }
    }
}