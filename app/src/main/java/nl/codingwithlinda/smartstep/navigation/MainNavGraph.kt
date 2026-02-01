package nl.codingwithlinda.smartstep.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dataStoreSettings
import nl.codingwithlinda.smartstep.core.data.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.features.main.MainScreen
import nl.codingwithlinda.smartstep.features.main.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.features.settings.presentation.UserSettingsRoot

@Composable
fun MainNavGraph(modifier: Modifier = Modifier) {

    val backStack = rememberNavBackStack(StartRoute)

    val shouldShowSettingsViewModel = viewModel<ShouldShowSettingsViewModel>(
        factory = viewModelFactory {
            initializer {
                ShouldShowSettingsViewModel(
                    userSettingsRepo = PreferencesUserSettingsRepo(
                        dataStore = dataStoreSettings
                    )
                )
            }
        }
    )
    val navTarget = shouldShowSettingsViewModel.shouldShowSettings.collectAsStateWithLifecycle().value

    AnimatedContent(navTarget) {
        if (it == null) {

        }
        else if (!it){
            backStack.retainAll (listOf(UserSettingsRoute))
        }
        else {
            backStack.remove(UserSettingsRoute)
            backStack.add(MainRoute)
        }
    }


    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = {
            when(it) {
                StartRoute -> NavEntry(StartRoute) {

                }
                UserSettingsRoute -> NavEntry(UserSettingsRoute){
                    UserSettingsRoot(
                        actionSkip = {
                            shouldShowSettingsViewModel.skip()
                        }
                    )
                }
                MainRoute -> NavEntry(MainRoute) {
                    MainScreen()
                }
                else -> error("Unknown route")
            }
        }
    )
}