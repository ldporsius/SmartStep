package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dataStoreSettings
import nl.codingwithlinda.smartstep.core.data.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.model.Gender
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme


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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    userSettings: UserSettings,
    actionSkip: () -> Unit,
    modifier: Modifier = Modifier) {


    var pickedGender by remember { mutableStateOf(userSettings.gender) }
    var isGenderExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {

                Text(text = "My profile",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                )

                TextButton(
                    onClick = { actionSkip()},
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text("Skip")
                }

        }

        Text(text = "This information helps calculate your activity more accurately.")
        with(userSettings) {
            ExposedDropdownMenuBox(
                expanded = isGenderExpanded,
                onExpandedChange = {
                    isGenderExpanded = it
                },

            ) {
                TextField(
                    value = pickedGender.name,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
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
            Text(text = "Height: $height")
            Text(text = "Weight: $weight")

        }
    }
}

@Preview
@Composable
private fun PreviewUserSettingsScreen() {
    SmartStepTheme {
        UserSettingsScreen(
            userSettings = UserSettings(),
            actionSkip = {},
            modifier = Modifier.fillMaxSize()
        )

    }
}