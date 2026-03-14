package nl.codingwithlinda.smartstep.features.main.navigation.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.SmartStepStateControllerImpl
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavActionControllerImpl
import nl.codingwithlinda.smartstep.features.steps_override_user.navigation.StepNavActionHandler


@Composable
fun navDrawerItems(): List<NavDrawerItem> {

    val navItemHandler = MainNavActionControllerImpl
    val stepsNavActionHandler = StepNavActionHandler

    val context = LocalContext.current

    val items: List<NavDrawerItem> = listOf(
        FixStepProblemNavItem(
            title = "Fix step problem",
            shouldShowInDrawer = {
                    !SmartStepStateControllerImpl.isIgnoringBattery(context)
            },
            mainNavActionController = navItemHandler
        ),
        DailyStepGoalNavItem(
            title = "Step goal",
            navActionController = navItemHandler
        ),
        PersonalSettingsNavItem(
            title = "Personal settings"
        ),

        EditStepsNavItem(
            title = "Edit steps",
            navActionController = stepsNavActionHandler
        ),
        ResetTodayStepsNavItem(
            title = "Reset today's steps",
            navActionController = stepsNavActionHandler
        )

    )

    return items
}