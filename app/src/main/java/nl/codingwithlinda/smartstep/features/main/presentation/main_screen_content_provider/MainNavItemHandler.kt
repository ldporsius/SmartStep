package nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.viewModelServiceLocator
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.ViewModelServiceLocator
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.design_system.components.CustomBottomSheet
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateControllerImpl
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.batteryOptimisation.presentation.AllowBackgroundAccessDialog
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalComponent
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalPickerContainer
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavActionController
import nl.codingwithlinda.smartstep.features.main.presentation.exit.ExitDialog
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavActionControllerImpl


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavItemHandler(
    dailyStepGoalViewModel: DailyStepGoalViewModel,
    mainNavAction: MainNavAction,
    navItemHandler: MainNavActionController = MainNavActionControllerImpl,
    smartStepStateController: SmartStepStateController,
) {


    when (mainNavAction) {
        MainNavAction.NA -> Unit

        MainNavAction.BACKGROUND_ACCESS_RECOMMENDED -> {

            fun handleResult() {
                smartStepStateController.onResult()
            }
            FormFactorWrapper(
                onDismiss = {
                    navItemHandler.handleAction(MainNavAction.NA)
                }
            ) {
                AllowBackgroundAccessDialog(
                    onResult = {
                        handleResult()
                    },
                    onDismiss = {
                        navItemHandler.handleAction(MainNavAction.NA)
                    }
                )
            }
        }

        MainNavAction.DAILY_STEP_GOAL -> {
            FormFactorWrapper(
                onDismiss = {
                    navItemHandler.handleAction(MainNavAction.NA)
                },
                useCustomBottomSheet = true
            ) {
                DailyStepGoalComponent(
                    dailyStepGoalViewModel = dailyStepGoalViewModel,
                    onDismiss = {
                        navItemHandler.handleAction(MainNavAction.NA)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                )
            }
        }

        MainNavAction.EXIT -> {
            ExitDialog(
                onDismiss = {
                    navItemHandler.handleAction(MainNavAction.NA)
                },
                onClick = {
                    smartStepStateController.exit()
                }
            )
        }
    }
}