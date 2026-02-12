package nl.codingwithlinda.smartstep.features.settings.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserSettingsWrapper(
    modifier: Modifier = Modifier,
    action: () -> Unit,
    content: @Composable () -> Unit,

) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My profile",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
        )

        Text(text = "Personal Settings")

        content()

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                action()
            },
            modifier = Modifier.fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Save")
        }
    }
}