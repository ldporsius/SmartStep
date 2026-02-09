package nl.codingwithlinda.smartstep.features.main.presentation.state

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavAction
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalComponent
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.exit.ExitDialog
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionUiState
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionsViewModel


val MainNavItemHandler = MainBottomSheetController()


@Composable
fun MainScreenDecorator(
    mainNavAction: MainNavAction,
    navItemHandler: MainBottomSheetController = MainNavItemHandler,
    permissionsViewModel: PermissionsViewModel,
    dailyStepGoalViewModel: DailyStepGoalViewModel,
    parent: BoxScope

) {
    when (mainNavAction) {
        MainNavAction.NA -> Unit

        MainNavAction.BACKGROUND_ACCESS_RECOMMENDED -> {
            permissionsViewModel.setPermissionState(PermissionUiState.BACKGROUND_ACCESS_RECOMMENDED)
        }

        MainNavAction.DAILY_STEP_GOAL -> {
            with(parent){
                DailyStepGoalComponent(
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
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        MainNavAction.EXIT -> {
            val activity = LocalActivity.current
            ExitDialog(
                onDismiss = {
                    navItemHandler.handleAction(MainNavAction.NA)
                },
                onClick = {
                    activity?.finishAffinity()
                    SmartStepApplication.killAll()
                }
            )
        }
    }

}