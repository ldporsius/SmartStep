package nl.codingwithlinda.smartstep.application.di.viewmodel_service

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.codingwithlinda.smartstep.application.di.AndroidDispatcherProvider
import nl.codingwithlinda.smartstep.application.di.AppContainer
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_count.DailyStepCountViewModel
import nl.codingwithlinda.smartstep.features.onboarding.presentation.ShouldShowSettingsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.EditStepsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.reset.presentation.ResetStepsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.walk_duration.presentation.WalkDurationViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation.WeeklyAverageViewModel

class ViewModelServiceLocator(
    val appContainer: AppContainer
) {

    fun createShouldShowSettingsViewModel(
        owner: ViewModelStoreOwner
    ): ShouldShowSettingsViewModel{
       val f =  viewModelFactoryHelper {
            ShouldShowSettingsViewModel(
                appContainer.userSettingsRepo
            )
        }
        val vm = ViewModelProvider.create(
            owner = owner,
            factory = f,

        )[ShouldShowSettingsViewModel::class]
        return vm
    }

    @Composable
    fun createDailyStepGoalViewModel(
    ): DailyStepGoalViewModel{
        val f =  viewModelFactoryHelper {
            DailyStepGoalViewModel(
                appScope = appContainer.applicationWideScope,
                dailyStepRepo = appContainer.dailyStepRepo
            )
        }
        val vm = viewModel<DailyStepGoalViewModel>(
            factory = f,
            )
        return vm
    }

    @Composable
    fun createDailyStepCountViewModel(): DailyStepCountViewModel{
        val f =  viewModelFactoryHelper {
            DailyStepCountViewModel(
                dailyStepRepo = appContainer.dailyStepRepo
            )
        }
        val vm = viewModel<DailyStepCountViewModel> (
            factory = f,
            )
        return vm
    }

    @Composable
    fun createEditStepsViewModel(): nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.EditStepsViewModel {
        val f =  viewModelFactoryHelper {
            _root_ide_package_.nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.EditStepsViewModel(
                dailyStepRepo = appContainer.dailyStepRepo,
                appScope = appContainer.applicationWideScope
            )
        }
        val vm = viewModel<nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.EditStepsViewModel>(
            factory = f,
            )
        return vm
    }

    @Composable
    fun createResetStepsViewModel(
        currentStep: DailyStepCount
    ): nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.reset.presentation.ResetStepsViewModel {
        val f =  viewModelFactoryHelper {
            _root_ide_package_.nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.reset.presentation.ResetStepsViewModel(
                dailyStepRepo = appContainer.dailyStepRepo,
                currentStep = currentStep,
                scope = appContainer.applicationWideScope
            )
        }
        val vm = viewModel<nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.reset.presentation.ResetStepsViewModel>(
            factory = f,
            )
        return vm
    }

    @Composable
    fun createWalkDurationViewModel(
    ): WalkDurationViewModel{
        val f =  viewModelFactoryHelper {
            WalkDurationViewModel(
                stepTracker = appContainer.stepTracker,
                walkDurationRepo = appContainer.walkDurationRepo,
                dispatcherProvider = AndroidDispatcherProvider()
            )
        }
        val vm = viewModel<WalkDurationViewModel>(
            factory = f,
            )
        return vm
    }

    @Composable
    fun createWeeklyAverageViewModel(): WeeklyAverageViewModel{
        val f =  viewModelFactoryHelper {
            WeeklyAverageViewModel(
                repo = appContainer.dailyStepRepo
            )
        }
        val vm = viewModel<WeeklyAverageViewModel>(
            factory = f,
            )
        return vm
    }


}