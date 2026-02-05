package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.core.domain.model.Gender
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design.ui.theme.white
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SettingBoxComponent
import nl.codingwithlinda.smartstep.features.settings.presentation.common.SettingsDialog
import nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings.GenderComponent
import nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings.GenderSettingsViewModel
import nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings.state.ActionGender
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.HeightSettingsComponent
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.HeightSettingsViewModel
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.HeightUnitConverter
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.WeightUnitConverter
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.WeightSettingViewModel
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.WeightSettingsScreen
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState


@Composable
fun UserSettingsRoot(
    userSettingsRepo: UserSettingsRepo,
    actionSkip: () -> Unit,
    modifier: Modifier = Modifier) {

    val genderSettingsViewModel = viewModel<GenderSettingsViewModel>(
        factory = viewModelFactory {
            initializer {
                GenderSettingsViewModel(
                    userSettingsRepo = userSettingsRepo
                )
            }
        }
    )
    val heightSettingsViewModel = viewModel<HeightSettingsViewModel>(
        factory = viewModelFactory {
            initializer {
                HeightSettingsViewModel(
                    userSettingsRepo = userSettingsRepo,
                    heightUnitConverter = HeightUnitConverter()
                )
            }
        }
    )
    val weightSettingsViewModel = viewModel<WeightSettingViewModel>(
        factory = viewModelFactory{
            initializer {
                WeightSettingViewModel(
                    userSettingsRepo = userSettingsRepo,
                    weightUnitConverter = WeightUnitConverter()
                )
            }
        }
    )


    UserSettingsScreen(
        modifier = modifier,
        userSettings = heightSettingsViewModel.userSettingsState.collectAsStateWithLifecycle().value,
        heightUiState = heightSettingsViewModel.heightUiState.collectAsStateWithLifecycle().value,
        weightUiState = weightSettingsViewModel.weightUiState.collectAsStateWithLifecycle().value,
        actionGenderInput = {
            genderSettingsViewModel.onAction(ActionGender.ChangeGender(it))
        },
        actionHeightInput = {
            heightSettingsViewModel.handleHeightInput(it)
        },
        actionWeightInput = {
            weightSettingsViewModel.onAction(it)
        },
        actionSkip = actionSkip,
        actionStart = {}
    )

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    userSettings: UserSettings,
    heightUiState: HeightSettingUiState,
    weightUiState: WeightSettingUiState,
    actionGenderInput: (Gender) -> Unit,
    actionHeightInput: (ActionHeightInput) -> Unit,
    actionWeightInput: (ActionWeightInput) -> Unit,
    actionSkip: () -> Unit,
    actionStart: () -> Unit,
    modifier: Modifier = Modifier) {


    var shouldShowHeightDialog by remember { mutableStateOf(false) }
    var shouldShowWeightDialog by remember { mutableStateOf(false) }


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
                    with(userSettings) {
                        GenderComponent(
                            currentGender = gender,
                            action = {
                                actionGenderInput(it)
                            }
                        )
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
                    with(weightUiState) {
                        SettingBoxComponent(
                            label = "Weight",
                            action = { shouldShowWeightDialog = !shouldShowWeightDialog },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = weightUiState.toUi().asString())
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    actionStart()
                },
                modifier = Modifier.fillMaxWidth(.75f)
                    .padding(16.dp)
            ) {
                Text("Start")
            }
        }
    }


    if (shouldShowHeightDialog){
        shouldShowWeightDialog = false
        SettingsDialog(
            onDismiss = { shouldShowHeightDialog = false }
        ){
            HeightSettingsComponent(
                uiState = heightUiState,
                action = {
                    actionHeightInput(it)
                },
                onCancel = {
                    shouldShowHeightDialog = false
                },

                onSave = {
                    shouldShowHeightDialog = false
                    actionHeightInput(ActionHeightInput.ActionSave)

                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    if (shouldShowWeightDialog){
        SettingsDialog(onDismiss = { shouldShowWeightDialog = false }) {
            WeightSettingsScreen(
                uiState = weightUiState,
                action = {
                    actionWeightInput(it)
                },
                onSave = {
                    shouldShowWeightDialog = false
                    actionWeightInput(ActionWeightInput.Save)

                },
                onCancel = {
                    shouldShowWeightDialog = false

                },
                modifier = Modifier
            )
        }
    }


}

@Preview
@Composable
private fun PreviewUserSettingsScreen() {
    SmartStepTheme {
        UserSettingsScreen(
            userSettings = UserSettings(),
            heightUiState = HeightSettingUiState.SI(175),
            weightUiState = WeightSettingUiState.SI(100),
            actionGenderInput = {},
            actionHeightInput = {},
            actionWeightInput = {},
            actionSkip = {},
            actionStart = {},
            modifier = Modifier.fillMaxSize(),
        )

    }
}