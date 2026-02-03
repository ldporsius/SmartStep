package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionUnitInput

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