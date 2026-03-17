package nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization.AllowBackgroundAccessDialog
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalComponent
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavActionController
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavActionControllerImpl
import nl.codingwithlinda.smartstep.features.main.presentation.exit.ExitDialog


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