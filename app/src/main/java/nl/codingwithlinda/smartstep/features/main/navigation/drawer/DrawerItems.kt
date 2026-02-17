package nl.codingwithlinda.smartstep.features.main.navigation.drawer

import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavActionController
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction
import nl.codingwithlinda.smartstep.features.steps.navigation.StepNavActionHandler
import nl.codingwithlinda.smartstep.navigation.NavigationController
import nl.codingwithlinda.smartstep.navigation.UserSettingsRoute


data class FixStepProblemNavItem(
    override val title: String,
    val shouldShowInDrawer: () -> Boolean,
    val mainNavActionController: MainNavActionController
): NavDrawerItem{

    override fun onAction(){
        mainNavActionController.handleAction(MainNavAction.BACKGROUND_ACCESS_RECOMMENDED)
    }

    override fun visible(): Boolean {
        return shouldShowInDrawer()
    }
}
data class DailyStepGoalNavItem(
    override val title: String,
    val navActionController: MainNavActionController
): NavDrawerItem{
    override fun onAction() {
        navActionController.handleAction(MainNavAction.DAILY_STEP_GOAL)
    }
    override fun visible(): Boolean = true
}

data class PersonalSettingsNavItem(
    override val title: String,
): NavDrawerItem {
    override fun onAction() {
        NavigationController.navigateTo(UserSettingsRoute)
    }

    override fun visible(): Boolean = true
}

data class EditStepsNavItem(
    override val title: String,
    val navActionController: StepNavActionHandler
): NavDrawerItem {
    override fun onAction() {
        navActionController.handleAction(StepNavAction.EDIT_STEPS)
    }
    override fun visible(): Boolean = true
}

data class ResetTodayStepsNavItem(
    override val title: String,
    val navActionController: StepNavActionHandler
): NavDrawerItem{
    override fun onAction() {
        navActionController.handleAction(StepNavAction.RESET_STEPS)
    }
    override fun visible(): Boolean = true
}
