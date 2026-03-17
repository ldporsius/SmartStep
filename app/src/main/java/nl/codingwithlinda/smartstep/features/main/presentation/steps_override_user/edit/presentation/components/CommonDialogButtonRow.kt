package nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommonDialogButtonRow(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.End
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