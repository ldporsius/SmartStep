package nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.design_system.components.CustomBottomSheet
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization.AllowBackgroundAccessDialog
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalComponent
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalPickerContainer
import nl.codingwithlinda.smartstep.features.main.domain.concrete_states.BackgroundRunningAllowed
import nl.codingwithlinda.smartstep.features.main.domain.concrete_states.BackgroundRunningDenied
import nl.codingwithlinda.smartstep.features.main.presentation.exit.ExitDialog
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavActionControllerImpl
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavItemHandler(
    mainNavAction: MainNavAction,
    navItemHandler: MainNavActionControllerImpl = MainNavActionControllerImpl,
    parent: BoxScope

) {

    val activity = LocalActivity.current
    val density = LocalDensity.current.density
    val isLargeScreen = LocalWindowInfo.current.containerSize.width > 840 * density

    @Composable
    fun getDailyStepGoal()=DailyStepGoalComponent(
        onDismiss = {
            navItemHandler.handleAction(MainNavAction.NA)
        },
        modifier = Modifier
    )

    when (mainNavAction) {
        MainNavAction.NA -> Unit

        MainNavAction.BACKGROUND_ACCESS_RECOMMENDED -> {

            val permissionsViewModel = viewModel<PermissionsViewModel>()
            fun handleResult(allowed: Boolean){
                when(allowed){
                    true -> {
                        activity?.let {
                            permissionsViewModel.setTrackingState(
                                BackgroundRunningAllowed(it)
                            )
                        }
                    }
                    false -> {
                        activity?.let {
                            permissionsViewModel.setTrackingState(
                                BackgroundRunningDenied(
                                    activity = activity,
                                    stepTracker = SmartStepApplication.stepTracker
                                )
                            )
                        }
                    }
                }
            }
            with(parent) {
                if (isLargeScreen){
                    Dialog(
                        onDismissRequest = {
                            navItemHandler.handleAction(MainNavAction.NA)
                        }
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            AllowBackgroundAccessDialog(
                                onResult = {allowed ->
                                   handleResult(allowed)
                                },
                                onDismiss = {
                                    navItemHandler.handleAction(MainNavAction.NA)
                                }
                            )
                        }
                    }
                }
                else{
                    ModalBottomSheet(
                        onDismissRequest = {
                            navItemHandler.handleAction(MainNavAction.NA)
                        }
                    ) {
                        AllowBackgroundAccessDialog(
                            onResult = {allowed ->
                                handleResult(allowed)
                            },
                            onDismiss = {
                                navItemHandler.handleAction(MainNavAction.NA)
                            }
                        )
                    }
                }
            }
        }

        MainNavAction.DAILY_STEP_GOAL -> {
            if (isLargeScreen) {
                Dialog(
                    onDismissRequest = {
                        navItemHandler.handleAction(MainNavAction.NA)
                    }
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp)
                    ) {
                       getDailyStepGoal()
                    }
                }
            } else {
                CustomBottomSheet(
                    onDismiss = {
                        navItemHandler.handleAction(MainNavAction.NA)
                    }
                ) {
                    DailyStepGoalPickerContainer(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        getDailyStepGoal()
                    }
                }
            }
        }

        MainNavAction.EXIT -> {
            val activity = LocalActivity.current
            ExitDialog(
                onDismiss = {
                    navItemHandler.handleAction(MainNavAction.NA)
                },
                onClick = {
                    activity?.let {ac ->
                        val trackerIntent = Intent(ac, StepTrackerService::class.java).apply {
                            action = StepTrackerService.ACTION_STOP
                        }
                        ac.startService(trackerIntent)
                    }

                    activity?.finish()

                }
            )
        }
    }
}