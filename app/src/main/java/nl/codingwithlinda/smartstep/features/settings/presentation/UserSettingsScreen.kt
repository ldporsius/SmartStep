package nl.codingwithlinda.smartstep.features.settings.presentation

import android.R.attr.text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dataStoreSettings
import nl.codingwithlinda.smartstep.core.data.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings


@Composable
fun UserSettingsRoot(
    actionSkip: () -> Unit,
    modifier: Modifier = Modifier) {
    val settingsViewModel = viewModel<UserSettingsViewModel>(
        factory = viewModelFactory {
            initializer {
                UserSettingsViewModel(
                    userSettingsRepo = PreferencesUserSettingsRepo(dataStoreSettings)
                )
            }
        }
    )

    UserSettingsScreen(
        userSettings = settingsViewModel.userSettings.collectAsStateWithLifecycle().value,
        actionSkip = actionSkip
    )

}
@Composable
fun UserSettingsScreen(
    userSettings: UserSettings,
    actionSkip: () -> Unit,
    modifier: Modifier = Modifier) {


    Column {
        Row {
            Text(text = "My profile")
            TextButton(
                onClick = { actionSkip()},
            ) {
                Text("Skip")
            }
        }
        Text(text = "This information helps calculate your activity more accurately.")
        with(userSettings) {
            Text(text = "Gender: $gender")
            Text(text = "Weight: $weight")
            Text(text = "Height: $height")
        }
    }
}