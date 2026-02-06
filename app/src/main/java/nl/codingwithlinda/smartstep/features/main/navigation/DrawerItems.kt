package nl.codingwithlinda.smartstep.features.main.navigation


data class FixStepProblemNavItem(
    override val title: String,
    val shouldShowInDrawer: () -> Boolean
): NavDrawerItem{

    override fun onAction(): MainNavAction {
        return MainNavAction.BACKGROUND_ACCESS_RECOMMENDED
    }

    override fun visible(): Boolean {
        return shouldShowInDrawer()
    }
}