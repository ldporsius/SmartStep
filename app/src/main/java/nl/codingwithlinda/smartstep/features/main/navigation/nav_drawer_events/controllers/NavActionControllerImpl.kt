package nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import nl.codingwithlinda.smartstep.features.main.navigation.controller.NavAction
import nl.codingwithlinda.smartstep.features.main.navigation.controller.NavActionController

object NavActionControllerImpl: NavActionController {
    private val _handleActionOservable = Channel<NavAction>()
    val actions = _handleActionOservable.receiveAsFlow()

    override fun handleAction(action: NavAction) {
        _handleActionOservable.trySend(action)
    }
}