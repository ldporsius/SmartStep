package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme

@Composable
fun HeightSettingsComponent(
    range: IntRange,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier) {

    var selectedIndex by remember { mutableStateOf(value) }

    val options = listOf<String>(
        "cm", "ft/in"
    )
    Column(
        modifier = modifier
    ) {

        SingleChoiceSegmentedButtonRow (
            modifier = Modifier.fillMaxWidth(),
        ){
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),

                    onClick = { selectedIndex = index },
                    selected = selectedIndex == index,
                ) {
                    Text(text = option)
                }
            }
        }


        Row {
            TextButton(
                onClick = {  }
            ) {
                Text("Cancel")
            }
            TextButton( onClick = {}
            ) {
                Text("OK")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHeightSettingsComponent() {
    SmartStepTheme {
        HeightSettingsComponent(
            range = 50..250,
            value = 180,
            onValueChange = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}