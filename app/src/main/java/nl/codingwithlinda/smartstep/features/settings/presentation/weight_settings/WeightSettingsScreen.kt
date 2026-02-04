package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogButtonRow
import nl.codingwithlinda.smartstep.features.settings.presentation.common.DialogHeader
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SelectableUnitsWeightUi
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SystemUnitSelector
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.ScrollableHeightInputComponent
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
        val selectableWeigths = SelectableUnitsWeightUi()
        SystemUnitSelector(
            options = selectableWeigths.units.map {
                it.toUi().asString()
            },
            isOptionSelected = {
                it == selectableWeigths.currentSelection
            },
            onClick = {
                selectableWeigths.currentSelection = it
                action(ActionWeightInput.ChangeSystem(selectableWeigths.units[it].system))
            }

        )

        when(uiState){
            is WeightSettingUiState.SI -> {
                ScrollableHeightInputComponent(
                    label = "kg",
                    defaultValue = uiState.kg,
                    values = List(51) {
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
                    values = List(51) {
                        it + 20
                    },
                    onValueChange = {
                        action(ActionWeightInput.PoundsInput(it))
                    }
                )
            }
        }

        DialogButtonRow(
            onDismiss = onCancel,
            onSave = onSave
        )
    }
}