package nl.codingwithlinda.smartstep.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dataStoreSettings
import nl.codingwithlinda.smartstep.core.data.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.features.main.MainScreen
import nl.codingwithlinda.smartstep.features.main.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.features.settings.presentation.UserSettingsRoot

@OptIn(ExperimentalMaterial3Api::class)
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
            //this is intended to avoid flickering screens
        }
        else if (!it){
            backStack.add(UserSettingsRoute)
        }
        else {
            backStack.add(MainRoute)
            backStack.retainAll(listOf(MainRoute))
        }
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = {
            when (it) {
                StartRoute -> NavEntry(StartRoute) {

                }

                UserSettingsRoute -> NavEntry(UserSettingsRoute) {
                    UserSettingsRoot(
                        modifier = Modifier.safeContentPadding(),
                        actionSkip = {
                            shouldShowSettingsViewModel.skip()
                            backStack.add(MainRoute)
                            backStack.retainAll(listOf(MainRoute))
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

    ObserveAsEvents(NavigationController.navEvents) {
        backStack.add(it)
    }

}