package nl.codingwithlinda.smartstep.features.settings.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserSettingsHeader(modifier: Modifier = Modifier) {
    Text(
        text = "My profile",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
    )

    Text(text = "Personal Settings")

}