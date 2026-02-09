package nl.codingwithlinda.smartstep.navigation

import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dataStoreSettings
import nl.codingwithlinda.smartstep.core.data.repo.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerImpl
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.features.main.presentation.MainScreen
import nl.codingwithlinda.smartstep.features.main.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalViewModel
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
    val shouldShowSettings = shouldShowSettingsViewModel.shouldShowSettings.collectAsStateWithLifecycle().value

    when(shouldShowSettings) {
        null -> Unit
        false -> {
            backStack.remove(StartRoute)
            backStack.add(UserSettingsRoute)
        }

       true -> {
            backStack.add(MainRoute)
            backStack.retainAll(listOf(MainRoute))
        }
    }

    val dailyStepGoalViewModel = viewModel<DailyStepGoalViewModel>(
        factory = viewModelFactory {
            initializer {
                DailyStepGoalViewModel(
                    appScope = SmartStepApplication.applicationScope,
                    dailyStepRepo = SmartStepApplication.dailyStepRepo,
                    stepTracker = StepTrackerImpl(
                        context = SmartStepApplication._applicationContext,
                        scope = SmartStepApplication.applicationScope
                    )
                )
            }
        }
    )

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = {
            when (it) {
                StartRoute -> NavEntry(StartRoute) {
                    Text("...")
                }

                UserSettingsRoute -> NavEntry(UserSettingsRoute) {
                    UserSettingsRoot(
                        userSettingsRepo = SmartStepApplication.userSettingsRepo,
                        modifier = Modifier.safeContentPadding(),
                        actionSkip = {
                            shouldShowSettingsViewModel.skip()
                            backStack.add(MainRoute)
                            backStack.retainAll(listOf(MainRoute))
                        }
                    )
                }

                MainRoute -> NavEntry(MainRoute) {
                    MainScreen(
                        dailyStepGoalViewModel = dailyStepGoalViewModel
                    )
                }

                else -> error("Unknown route")
            }
        }
    )

    ObserveAsEvents(NavigationController.navEvents) {
        backStack.add(it)
    }

}