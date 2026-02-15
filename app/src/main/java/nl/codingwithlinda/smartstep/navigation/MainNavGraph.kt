package nl.codingwithlinda.smartstep.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dataStoreSettings
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.userSettingsRepo
import nl.codingwithlinda.smartstep.core.data.repo.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.features.onboarding.presentation.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.MainScreen
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.onboarding.presentation.UserSettingsOnboardingWrapper
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.UserSettingsRoot
import nl.codingwithlinda.smartstep.features.settings.presentation.common.UserSettingsWrapper

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
            backStack.add(MainRoute)
            backStack.retainAll(listOf(MainRoute))
        }
        true -> {
            backStack.add(UserSettingsOnboardingRoute)
            backStack.remove(StartRoute)

        }
    }

    val dailyStepGoalViewModel = viewModel<DailyStepGoalViewModel>(
        factory = viewModelFactory {
            initializer {
                DailyStepGoalViewModel(
                    appScope = SmartStepApplication.applicationScope,
                    dailyStepRepo = SmartStepApplication.dailyStepRepo,
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

                UserSettingsOnboardingRoute -> NavEntry(UserSettingsRoute) {
                    UserSettingsOnboardingWrapper(
                        modifier = Modifier
                            .fillMaxWidth()
                            .safeContentPadding()

                        ,
                        onSkip = {
                            shouldShowSettingsViewModel.skip()
                            backStack.add(MainRoute)
                            backStack.retainAll(listOf(MainRoute))
                        },
                        action = {
                            SmartStepApplication.applicationScope.launch {
                                val userSettings = UserSettingsMemento.restoreLast()
                                userSettingsRepo.saveSettings(userSettings)
                                userSettingsRepo.setIsOnboardingFalse()
                                NavigationController.navigateTo(MainRoute)
                            }
                        }
                    ) {
                        UserSettingsRoot(
                            userSettingsRepo = SmartStepApplication.userSettingsRepo,
                            modifier = Modifier.width(480.dp)
                        )
                    }
                }
                UserSettingsRoute -> NavEntry(UserSettingsRoute) {
                    UserSettingsWrapper(
                        modifier = Modifier
                            .safeContentPadding()
                            .width(480.dp)
                            .fillMaxHeight()
                        ,
                        action = {
                            SmartStepApplication.applicationScope.launch {
                                val userSettings = UserSettingsMemento.restoreLast()
                                userSettingsRepo.saveSettings(userSettings)
                                NavigationController.navigateTo(MainRoute)
                            }
                        }
                    ) {
                        UserSettingsRoot(
                            userSettingsRepo = SmartStepApplication.userSettingsRepo,
                        )
                    }
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