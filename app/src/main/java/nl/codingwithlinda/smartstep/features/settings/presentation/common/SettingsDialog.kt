package nl.codingwithlinda.smartstep.features.settings.presentation.common

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
   ) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(.5f),
            shape = MaterialTheme.shapes.medium
        ) {
            content()
        }
    }
}