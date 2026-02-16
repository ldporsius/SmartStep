package nl.codingwithlinda.smartstep.features.main.navigation.drawer

interface NavDrawerItem {
    val title: String
    fun onAction()

    fun visible(): Boolean

}