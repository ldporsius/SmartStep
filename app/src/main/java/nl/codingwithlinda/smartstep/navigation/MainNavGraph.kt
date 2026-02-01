package nl.codingwithlinda.smartstep.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import nl.codingwithlinda.smartstep.features.settings.presentation.UserSettingsScreen

@Composable
fun MainNavGraph(modifier: Modifier = Modifier) {

    val backStack = rememberNavBackStack(UserSettingsRoute)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = {
            when(it) {
                UserSettingsRoute -> NavEntry(UserSettingsRoute){
                    UserSettingsScreen()
                }
                else -> error("Unknown route")
            }
        }
    )
}