package nl.codingwithlinda.smartstep.features.main.navigation.drawer

import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavActionController
import nl.codingwithlinda.smartstep.features.main.navigation.controller.NavActionController
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction


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

data class EditStepsNavItem(
    override val title: String,
    val navActionController: NavActionController
): NavDrawerItem {
    override fun onAction() {
        navActionController.handleAction(StepNavAction.EDIT_STEPS)
    }
    override fun visible(): Boolean = true
}
