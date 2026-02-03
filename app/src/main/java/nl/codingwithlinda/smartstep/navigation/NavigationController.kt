package nl.codingwithlinda.smartstep.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object NavigationController {

   private val _navEvents = Channel<NavKey>()
    val navEvents = _navEvents.receiveAsFlow()

    fun navigateTo(navKey: NavKey){
        _navEvents.trySend(navKey)
    }

}