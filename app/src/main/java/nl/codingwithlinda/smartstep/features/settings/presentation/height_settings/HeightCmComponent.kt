package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HeightCmComponent(
    range: IntRange,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState(
                initial = value
            )),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        range.onEach {
            Text(
                text = it.toString(),
                modifier = Modifier.clickable {
                    onValueChange(it)
                }
            )
        }
    }
}