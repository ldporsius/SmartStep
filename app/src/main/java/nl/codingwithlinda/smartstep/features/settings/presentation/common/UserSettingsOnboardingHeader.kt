package nl.codingwithlinda.smartstep.features.settings.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun UserSettingsOnboardingHeader(
    onSkip: () -> Unit,
    modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "My profile",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
        )

        TextButton(
            onClick = { onSkip() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text("Skip")
        }

    }

    Text(text = "This information helps calculate your activity more accurately.")

}