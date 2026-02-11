package nl.codingwithlinda.smartstep.features.main.presentation.state

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavAction
import nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization.ShowBackgroundAccessDialog
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalComponent
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalPickerContainer
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.exit.ExitDialog


val MainNavItemHandler = MainBottomSheetController()


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenDecorator(
    mainNavAction: MainNavAction,
    navItemHandler: MainBottomSheetController = MainNavItemHandler,
    dailyStepGoalViewModel: DailyStepGoalViewModel,
    parent: BoxScope

) {

    val density = LocalDensity.current.density
    val isLargeScreen = LocalWindowInfo.current.containerSize.width > 840 * density

    @Composable
    fun getDailyStepGoal()=DailyStepGoalComponent(
        selectedGoal = dailyStepGoalViewModel.goal.collectAsStateWithLifecycle().value,
        onSelected = {
            dailyStepGoalViewModel.setGoal(it)
        },
        onSave = {
            dailyStepGoalViewModel.saveGoal(it)
            navItemHandler.handleAction(MainNavAction.NA)
        },
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
                            ShowBackgroundAccessDialog(
                                visible = true,
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
                        ShowBackgroundAccessDialog(
                            visible = true,
                            onDismiss = {
                                navItemHandler.handleAction(MainNavAction.NA)
                            }
                        )
                    }
                }
            }
        }

        MainNavAction.DAILY_STEP_GOAL -> {
            with(parent) {
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
                    DailyStepGoalPickerContainer(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
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