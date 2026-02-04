package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dataStoreSettings
import nl.codingwithlinda.smartstep.core.data.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.model.Gender
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design.ui.theme.white
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.HeightSettingsComponent
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionUnitInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.HeightUnitConverter


@Composable
fun UserSettingsRoot(
    actionSkip: () -> Unit,
    modifier: Modifier = Modifier) {
    val settingsViewModel = viewModel<UserSettingsViewModel>(
        factory = viewModelFactory {
            initializer {
                UserSettingsViewModel(
                    userSettingsRepo = PreferencesUserSettingsRepo(dataStoreSettings),
                    heightUnitConverter = HeightUnitConverter()
                )
            }
        }
    )

    UserSettingsScreen(
        modifier = modifier,
        userSettings = settingsViewModel.userSettingsState.collectAsStateWithLifecycle().value,
        heightUiState = settingsViewModel.heightUiState.collectAsStateWithLifecycle().value,
        unitChoice = settingsViewModel.unitChoice.collectAsStateWithLifecycle().value,
        onUnitChange = {
            settingsViewModel.onUnitChange(it)
        },
        actionUnitInput = {
            settingsViewModel.handleHeightInput(it)
        },
        actionSkip = actionSkip
    )

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    userSettings: UserSettings,
    heightUiState: HeightSettingUiState,
    unitChoice: UnitSystemUnits,
    onUnitChange: (UnitSystemUnits) -> Unit,
    actionUnitInput: (ActionUnitInput) -> Unit,
    actionSkip: () -> Unit,
    modifier: Modifier = Modifier) {


    var pickedGender by remember { mutableStateOf(Gender.FEMALE) }
    var isGenderExpanded by rememberSaveable { mutableStateOf(false) }
    var shouldShowHeightDialog by remember { mutableStateOf(false) }


    Surface {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "My profile",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
            )

            TextButton(
                onClick = { actionSkip() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("Skip")
            }

        }

        Text(text = "This information helps calculate your activity more accurately.")

        with(userSettings) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = white,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)

            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isGenderExpanded,
                        onExpandedChange = {
                            isGenderExpanded = it
                        },

                        ) {

                        SettingBoxComponent(
                            label = "Gender",
                            modifier = Modifier.clickable(
                                onClick = { isGenderExpanded = true }
                            )
                        ) {
                            Text(text = "$pickedGender",
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )
                        }

                        ExposedDropdownMenu(
                            expanded = isGenderExpanded,
                            onDismissRequest = { isGenderExpanded = false }
                        ) {
                            Gender.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.name) },
                                    onClick = {
                                        pickedGender = it
                                        isGenderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    with(heightUiState) {
                        SettingBoxComponent(
                            label = "Height",
                            action = { shouldShowHeightDialog = !shouldShowHeightDialog },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = heightUiState.toUi().asString())
                        }
                    }
                    SettingBoxComponent(
                        label = "Weight",
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "$weight")
                    }
                }
            }

        }
    }
    }


    if (shouldShowHeightDialog){
        Dialog(
            onDismissRequest = { shouldShowHeightDialog = false }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(.5f),
                shape = MaterialTheme.shapes.medium
            ) {
                HeightSettingsComponent(
                    uiState = heightUiState,
                    unitChoice = unitChoice,
                    onUnitChange = {
                        onUnitChange(it)
                    },
                    onValueChange = {
                        actionUnitInput(it)
                    },
                    onCancel = {
                        shouldShowHeightDialog = false
                    },

                    onSave = {
                        shouldShowHeightDialog = false
                        actionUnitInput(ActionUnitInput.ActionSave)

                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

}

@Preview
@Composable
private fun PreviewUserSettingsScreen() {
    SmartStepTheme {
        UserSettingsScreen(
            userSettings = UserSettings(),
            heightUiState = HeightSettingUiState.Imperial(feet = 5, inches = 9),
            actionUnitInput = {},
            actionSkip = {},
            modifier = Modifier.fillMaxSize(),
            unitChoice = UnitSystemUnits.FEET_INCHES,
            onUnitChange = {}
        )

    }
}