package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionUnitInput
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.toUi

@Composable
fun HeightSettingsComponent(
    range: IntRange,
    unitChoice: UnitSystemUnits,
    onUnitChange: (UnitSystemUnits) -> Unit,
    value: Int,
    onValueChange: (ActionUnitInput) -> Unit,
    modifier: Modifier = Modifier) {

   // var selectedIndex by remember { mutableStateOf(value) }

    val options = listOf<UnitSystemUnits>(
        UnitSystemUnits.CM,
        UnitSystemUnits.FEET_INCHES
    )
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {

        SingleChoiceSegmentedButtonRow (
            modifier = Modifier.fillMaxWidth(),
        ){
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),

                    onClick = { onUnitChange(option) },
                    selected = option == unitChoice,
                ) {
                    Text(text = option.toUi().asString())
                }
            }
        }

        when(unitChoice){
            UnitSystemUnits.CM -> {
                ScrollableHeightInputComponent(
                    label = unitChoice.toUi().asString(),
                    defaultValue = 170,
                    values = List(51){
                        it + 160
                    },
                    onValueChange = {
                        onValueChange(ActionUnitInput.CmInput(it))
                    },
                    modifier = Modifier,
                )
            }
            UnitSystemUnits.FEET_INCHES -> {
                HeightFeetInchesComponent(
                    feetComponent = {
                        ScrollableHeightInputComponent(
                            label = "ft",
                            defaultValue = 5,
                            values = List(11){
                                it
                            },
                            onValueChange = {
                                onValueChange(ActionUnitInput.FeetInput(it))
                            },
                            modifier = Modifier,
                        )
                    },
                    inchesComponent = {
                        ScrollableHeightInputComponent(
                            label = "in",
                            defaultValue = 8,
                            values = List(11){
                                it
                            },
                            onValueChange = {
                                onValueChange(ActionUnitInput.InchesInput(it))
                            },
                            modifier = Modifier,
                        )
                    }
                )
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
    var unitChoice: UnitSystemUnits by remember { mutableStateOf(UnitSystemUnits.FEET_INCHES) }

    SmartStepTheme {
        HeightSettingsComponent(
            range = 50..250,
            value = 180,
            onValueChange = {},
            modifier = Modifier.fillMaxSize(),
            unitChoice = unitChoice,
            onUnitChange = {
                unitChoice = it
            }
        )
    }
}