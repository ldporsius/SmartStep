package nl.codingwithlinda.smartstep.features.settings.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DialogHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
) {
    Column(modifier = modifier) {

        Text(text = title, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Text(text = subtitle)
    }
}