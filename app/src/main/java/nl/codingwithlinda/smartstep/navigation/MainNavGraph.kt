package nl.codingwithlinda.smartstep.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.di.AppContainer
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.viewModelFactoryHelper
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.SmartStepStateControllerImpl
import nl.codingwithlinda.smartstep.core.presentation.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.design_system.ui.theme.bg
import nl.codingwithlinda.smartstep.features.ai_integration.data.local_cache.AIChatRepoImpl
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.AIChatRoot
import nl.codingwithlinda.smartstep.features.ai_integration.features.passive.data.AIUserMessages
import nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation.AIMessageComponent
import nl.codingwithlinda.smartstep.features.main.presentation.MainScreen
import nl.codingwithlinda.smartstep.features.onboarding.presentation.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.features.onboarding.presentation.UserSettingsOnboardingWrapper
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.UserSettingsRoot
import nl.codingwithlinda.smartstep.features.settings.presentation.common.UserSettingsWrapper
import nl.codingwithlinda.smartstep.features.statistics.presentation.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    appContainer: AppContainer,
    smartStepStateController: SmartStepStateControllerImpl,
    modifier: Modifier = Modifier) {

    val backStack = rememberNavBackStack(StartRoute)




    val shouldShowSettingsViewModel = viewModel<ShouldShowSettingsViewModel>(
        factory = viewModelFactoryHelper {
            ShouldShowSettingsViewModel(
                appContainer.userSettingsRepo
            )
        }
    )


    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider{

            entry<StartRoute>{

                shouldShowSettingsViewModel.check()
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


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = bg)
                ){
                    Text("...")
                }

            }
            entry<UserSettingsOnboardingRoute> {
                val userSettingsRepo = appContainer.userSettingsRepo
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
                        appContainer.applicationWideScope.launch {
                            val userSettings = UserSettingsMemento.restoreLast()
                            userSettingsRepo.saveSettings(userSettings)
                            userSettingsRepo.setIsOnboardingFalse()
                            NavigationController.navigateTo(MainRoute)
                        }
                    }
                ) {
                    UserSettingsRoot(
                        userSettingsRepo = appContainer.userSettingsRepo,
                        modifier = Modifier.width(480.dp)
                    )
                }
            }
            entry<UserSettingsRoute> {
                UserSettingsWrapper(
                    modifier = Modifier
                        .safeContentPadding()
                        .width(480.dp)
                        .fillMaxHeight()
                    ,
                    action = {
                        appContainer.applicationWideScope.launch {
                            val userSettings = UserSettingsMemento.restoreLast()
                            appContainer.userSettingsRepo.saveSettings(userSettings)
                            NavigationController.navigateTo(MainRoute)
                        }
                    }
                ) {
                    UserSettingsRoot(
                        userSettingsRepo = appContainer.userSettingsRepo ,
                    )
                }
            }

            entry<MainRoute> {
                shouldShowSettingsViewModel.check()
                val dailyStepGoalViewModel = SmartStepApplication
                    .viewModelServiceLocator.createDailyStepGoalViewModel()

                val dailyStepCountViewModel = SmartStepApplication
                    .viewModelServiceLocator.createDailyStepCountViewModel()


                val editStepsViewModel = SmartStepApplication
                    .viewModelServiceLocator.createEditStepsViewModel()

                val resetStepsViewModel = SmartStepApplication
                    .viewModelServiceLocator.createResetStepsViewModel(
                        currentStep = dailyStepCountViewModel.todaysStep.collectAsStateWithLifecycle().value ?: DailyStepCountCreator.create(0)
                    )
                val walkDurationViewModel = SmartStepApplication.viewModelServiceLocator.createWalkDurationViewModel()

                val weeklyAverageViewModel = SmartStepApplication.viewModelServiceLocator.createWeeklyAverageViewModel()


                val statisticsViewModel = viewModel<StatisticsViewModel>(
                    factory = viewModelFactory {
                        initializer {
                            StatisticsViewModel(
                                statisticsManager = SmartStepApplication.appContainer.statisticsManager
                            )
                        }
                    }
                )

                @Composable
                fun aiMessageComponent() =
                    AIMessageComponent(
                       aiStateController = appContainer.AIContainer.aiStateControllerGroqPassive,
                        aiUserMessages = AIUserMessages(
                            dailyStepRepo = appContainer.dailyStepRepo,
                            userSettingsRepo = appContainer.userSettingsRepo
                        ),
                        onMore = {
                            appContainer.AIContainer.aiStateControllerGroqActive.aiChatRepo.clearHistory()
                            backStack.remove(AIChatRoute)
                            backStack.add(AIChatRoute)

                        }
                    )


                MainScreen(
                    dailyStepGoalViewModel = dailyStepGoalViewModel,
                    dailyStepCountViewModel = dailyStepCountViewModel,
                    statisticsViewModel = statisticsViewModel,
                    stepTrackerViewModel = walkDurationViewModel,
                    weeklyAverageViewModel = weeklyAverageViewModel,
                    editStepsViewModel = editStepsViewModel,
                    resetStepsViewModel = resetStepsViewModel,
                    smartStepStateController = smartStepStateController,
                    aiMessageComponent = {
                        aiMessageComponent()
                    }
                )
            }


            entry<AIChatRoute>{
                AIChatRoot(
                    aiStateController = appContainer.AIContainer.aiStateControllerGroqActive,
                    userSettingsRepo = appContainer.userSettingsRepo,
                    onNavBack = {
                        backStack.remove(AIChatRoute)
                    }
                )
            }
        }
    )

    ObserveAsEvents(NavigationController.navEvents) {
        backStack.remove(StartRoute)
        backStack.add(it)
    }

}