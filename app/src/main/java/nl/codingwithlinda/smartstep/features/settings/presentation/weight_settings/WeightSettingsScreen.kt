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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.features.main.presentation.common.CommonNumberPicker
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogButtonRow
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogHeader
import nl.codingwithlinda.smartstep.features.settings.presentation.common.ScrollableInputComponent
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.toUi
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState

@Composable
fun WeightSettingsScreen(
    uiState: WeightSettingUiState,
    valuesKg: List<Int>,
    valuesPounds: List<Int>,
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
                SegmentedButton(modifier = Modifier
                    .semantics(){
                        contentDescription = "option$index"
                    },
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
                    CommonNumberPicker(
                        label = "kg",
                        values = valuesKg,
                        selectedGoal = uiState.roundedKg,
                        onGoalSelected = {
                            action(ActionWeightInput.KgInput(it))
                        }
                    )
                }

                is WeightSettingUiState.Imperial -> {
                    CommonNumberPicker(
                        label = "lbs",
                        values = valuesPounds,
                        selectedGoal = uiState.pounds,
                        onGoalSelected = {
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