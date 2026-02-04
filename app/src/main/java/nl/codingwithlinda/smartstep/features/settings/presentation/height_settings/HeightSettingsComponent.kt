package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.layout.Box
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
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.LengthUnits
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionUnitInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.toUi

@Composable
fun HeightSettingsComponent(
    uiState: HeightSettingUiState,
    onUnitChange: (LengthUnits) -> Unit,
    onValueChange: (ActionUnitInput) -> Unit,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
    modifier: Modifier = Modifier) {


    val options = listOf<LengthUnits>(
        LengthUnits.CM,
        LengthUnits.FEET_INCHES
    )
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {

        Text(text = "Height", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Text(text = "Used to calculate distance")

        SingleChoiceSegmentedButtonRow (
            modifier = Modifier.fillMaxWidth(),
        ){
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),

                    onClick = { onUnitChange(option) },
                    selected = option == uiState.system,
                ) {
                    Text(text = option.toUi().asString())
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (uiState) {
                is HeightSettingUiState.SI -> {
                    ScrollableHeightInputComponent(
                        label = "cm",
                        defaultValue = uiState.valueCm,
                        values = List(51) {
                            it + 160
                        },
                        onValueChange = {
                            onValueChange(ActionUnitInput.CmInput(it))
                        },
                        modifier = Modifier,
                    )
                }

                is HeightSettingUiState.Imperial -> {
                    println("--- HEIGHTSETTINGSCOMPONENT --- imperial uiState: $uiState")
                    HeightFeetInchesComponent(
                        feetComponent = {
                            ScrollableHeightInputComponent(
                                label = "ft",
                                defaultValue = uiState.feet,
                                values = List(8) {
                                    it
                                },
                                onValueChange = {
                                    onValueChange(
                                        ActionUnitInput.ImperialInput(
                                            feet = it,
                                            inches = uiState.inches
                                        )
                                    )
                                },
                                modifier = Modifier,
                            )
                        },
                        inchesComponent = {
                            ScrollableHeightInputComponent(
                                label = "in",
                                defaultValue = uiState.inches,
                                values = List(12) {
                                    it
                                },
                                onValueChange = {
                                    onValueChange(
                                        ActionUnitInput.ImperialInput(
                                            feet = uiState.feet,
                                            inches = it
                                        )
                                    )
                                },
                                modifier = Modifier,
                            )
                        }
                    )
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
        ) {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text("Cancel")
            }
            TextButton( onClick = {
                onSave()
            }
            ) {
                Text("OK")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHeightSettingsComponent() {
    var unitChoice: LengthUnits by remember { mutableStateOf(LengthUnits.FEET_INCHES) }

    SmartStepTheme {
        HeightSettingsComponent(
            uiState = HeightSettingUiState.Imperial(175),
            onValueChange = {},
            modifier = Modifier.fillMaxSize(),
            onUnitChange = {
                unitChoice = it
            }
        )
    }
}