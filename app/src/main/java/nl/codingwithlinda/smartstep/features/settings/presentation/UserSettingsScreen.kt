package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dataStoreSettings
import nl.codingwithlinda.smartstep.core.data.PreferencesUserSettingsRepo

@Composable
fun UserSettingsScreen(modifier: Modifier = Modifier) {

    val settingsViewModel = viewModel<UserSettingsViewModel>(
        factory = viewModelFactory {
            initializer {
                UserSettingsViewModel(
                    userSettingsRepo = PreferencesUserSettingsRepo(dataStoreSettings)
                )
            }
        }
    )

    Column {
        with(settingsViewModel.userSettings.collectAsStateWithLifecycle().value) {
            Text(text = "Gender: $gender")
            Text(text = "Weight: $weight")
            Text(text = "Height: $height")
        }
    }
}