package nl.codingwithlinda.smartstep.features.main.navigation.drawer

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization.isIgnoringBatteryOptimizations
import nl.codingwithlinda.smartstep.features.main.presentation.nav_drawer_events.controllers.MainNavItemHandler
import nl.codingwithlinda.smartstep.features.main.presentation.nav_drawer_events.controllers.NavActionControllerImpl


@Composable
fun navDrawerItems(): List<NavDrawerItem> {

    val activity = LocalActivity.current
    val navItemHandler = MainNavItemHandler
    val stepsNavActionController = NavActionControllerImpl

    val items: List<NavDrawerItem> = listOf(
        FixStepProblemNavItem(
            title = "Fix step problem",
            shouldShowInDrawer = {
                activity?.let {
                    isIgnoringBatteryOptimizations(it)
                }?.not() ?: false
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
            navActionController = stepsNavActionController
        ),
        ResetTodayStepsNavItem(
            title = "Reset today's steps",
            navActionController = stepsNavActionController
        )

    )

    return items
}