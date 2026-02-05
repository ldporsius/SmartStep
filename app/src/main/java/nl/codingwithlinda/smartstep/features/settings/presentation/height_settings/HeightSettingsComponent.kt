package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.LengthUnits
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogButtonRow
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogHeader
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SelectableUnitsLengthUi
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SystemUnitSelector
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.toUi

@Composable
fun HeightSettingsComponent(
    uiState: HeightSettingUiState,
    action: (ActionHeightInput) -> Unit,
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

        DialogHeader(
        title = "Height",
        subtitle = "Used to calculate distance"
        )
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier.fillMaxWidth(),
        ){
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = {
                        action(ActionHeightInput.ChangeUnitSystem(option.system))
                    },
                    selected = option.system == uiState.system,
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
                            action(ActionHeightInput.CmInput(it))
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
                                    action(
                                        ActionHeightInput.ImperialInput(
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
                                    action(
                                        ActionHeightInput.ImperialInput(
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

        DialogButtonRow(
            onDismiss = onCancel,
            onSave = onSave
        )
    }
}

@Preview
@Composable
private fun PreviewHeightSettingsComponent() {

    SmartStepTheme {
        HeightSettingsComponent(
            uiState = HeightSettingUiState.Imperial(175),
            action = {},
            modifier = Modifier.fillMaxSize(),

        )
    }
}