package nl.codingwithlinda.smartstep.features.main.presentation.nav_drawer_events

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavActionController

class MainScreenDecorator: MainNavActionController {
    private val _handleActionOservable = Channel<MainNavAction>()
    val actions = _handleActionOservable.receiveAsFlow()

    override fun handleAction(action: MainNavAction) {
       _handleActionOservable.trySend(action)
    }

}