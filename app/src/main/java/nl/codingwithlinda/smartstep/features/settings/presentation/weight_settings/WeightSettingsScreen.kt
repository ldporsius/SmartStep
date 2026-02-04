package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.LengthUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogButtonRow
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogHeader
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SelectableUnitsWeightUi
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SystemUnitSelector
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.ScrollableHeightInputComponent
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.toUi
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState

@Composable
fun WeightSettingsScreen(
    uiState: WeightSettingUiState,
    action: (ActionWeightInput) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        DialogHeader(
            title = "Weight",
            subtitle = "Used to calculate calories"
        )

        val options = listOf(
            WeightUnits.KG,
            WeightUnits.LBS
        )
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier.fillMaxWidth(),
        ){
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = {
                        action(ActionWeightInput.ChangeSystem(option.system))
                    },
                    selected = option.system == uiState.system,
                ) {
                    Text(text = option.toUi().asString())
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (uiState) {
                is WeightSettingUiState.SI -> {
                    ScrollableHeightInputComponent(
                        label = "kg",
                        defaultValue = uiState.kg,
                        values = List(200) {
                            it + 10
                        },
                        onValueChange = {
                            action(ActionWeightInput.KgInput(it))
                        },
                    )
                }

                is WeightSettingUiState.Imperial -> {
                    ScrollableHeightInputComponent(
                        label = "lbs",
                        defaultValue = uiState.pounds,
                        values = List(300) {
                            it + 40
                        },
                        onValueChange = {
                            action(ActionWeightInput.PoundsInput(it))
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