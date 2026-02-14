package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.LengthUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsCm
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsInches
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.main.presentation.common.CommonNumberPicker
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogButtonRow
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogHeader
import nl.codingwithlinda.smartstep.features.settings.presentation.common.ScrollableInputComponent
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.toUi

@Composable
fun HeightSettingsComponent(
    uiState: HeightSettingUiState,
    rangeCm: List<Int>,
    rangeFeet: List<Int>,
    rangeInches: List<Int>,
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
        horizontalAlignment = Alignment.CenterHorizontally
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
                    CommonNumberPicker(
                        label = "cm",
                        values = rangeCm,
                        selectedGoal = uiState.valueCm,
                        onGoalSelected = {
                            action(ActionHeightInput.CmInput(it))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is HeightSettingUiState.Imperial -> {

                    BoxWithConstraints() {
                        val halfWidth = maxWidth / 2

                        HeightFeetInchesComponent(
                            feetComponent = {
                                CommonNumberPicker(
                                    label = "ft",
                                    values = rangeFeet,
                                    selectedGoal = uiState.feet,
                                    onGoalSelected = {
                                        action(
                                            ActionHeightInput.ImperialInput(
                                                feet = it,
                                                inches = uiState.inches
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .width(halfWidth)
                                        .semantics {
                                            contentDescription = "feet"
                                        },
                                )
                            },
                            inchesComponent = {
                                CommonNumberPicker(
                                    label = "in",
                                    values = rangeInches,
                                    selectedGoal = uiState.inches,
                                    onGoalSelected = {
                                        action(
                                            ActionHeightInput.ImperialInput(
                                                feet = uiState.feet,
                                                inches = it
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .width(halfWidth)
                                        .semantics {
                                        contentDescription = "inches"
                                    },
                                )

                            }
                        )
                    }
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
            rangeCm = heightsCm.toList(),
            rangeFeet = heightsFeet.toList(),
            rangeInches = heightsInches.toList(),
            action = {},
            modifier = Modifier.fillMaxSize(),

        )
    }
}