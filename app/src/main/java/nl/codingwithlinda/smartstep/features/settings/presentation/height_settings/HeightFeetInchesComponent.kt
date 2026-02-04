package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HeightFeetInchesComponent(
    feetComponent: @Composable () -> Unit,
    inchesComponent: @Composable () -> Unit,
    modifier: Modifier = Modifier) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        feetComponent()
        inchesComponent()
    }
}