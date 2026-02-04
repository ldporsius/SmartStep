package nl.codingwithlinda.smartstep.features.settings.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SystemUnitSelector(
    options: List<String>,
    isOptionSelected: (Int) -> Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier) {
    SingleChoiceSegmentedButtonRow (
        modifier = modifier.fillMaxWidth(),
    ){
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),

                onClick = {
                    onClick(index)
                          },
                selected = isOptionSelected(index),
            ) {
                Text(text = option)
            }
        }
    }
}