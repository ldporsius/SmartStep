package nl.codingwithlinda.smartstep.features.steps.presentation.state

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction

object StepNavActionHandler {
    private val _handleActionOservable = Channel<StepNavAction>()
    val actions = _handleActionOservable.receiveAsFlow()

    fun handleAction(action: StepNavAction) {
        _handleActionOservable.trySend(action)
    }
}