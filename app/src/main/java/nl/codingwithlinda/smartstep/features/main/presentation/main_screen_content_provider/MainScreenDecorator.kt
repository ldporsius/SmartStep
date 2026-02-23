package nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.design_system.components.CustomBottomSheet
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization.AllowBackgroundAccessDialog
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalComponent
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalPickerContainer
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.exit.ExitDialog
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavActionControllerImpl
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavItemHandler


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenDecorator(
    mainNavAction: MainNavAction,
    navItemHandler: MainNavActionControllerImpl = MainNavItemHandler,
    parent: BoxScope

) {

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