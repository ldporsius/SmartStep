package nl.codingwithlinda.smartstep.features.main.navigation

import androidx.compose.runtime.Composable
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavAction

interface NavDrawerItem {
    val title: String
    fun onAction(): MainNavAction

    fun visible(): Boolean

}