package nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.reset.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResetStepsDialog(
    onDismiss: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(48.dp)

    ) {

        Text("Are you sure you want to reset today's steps?",
            style = MaterialTheme.typography.titleMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically

        ) {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
            TextButton(
                onClick = onReset
            ) {
                Text("Reset")
            }

        }

    }
}