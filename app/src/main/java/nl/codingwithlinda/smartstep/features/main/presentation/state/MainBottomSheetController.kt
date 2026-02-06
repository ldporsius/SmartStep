package nl.codingwithlinda.smartstep.features.main.presentation.state

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavActionController


class MainBottomSheetController: MainNavActionController {
    private val _handleActionOservable = Channel<MainNavAction>()
    val actions = _handleActionOservable.receiveAsFlow()

    override fun handleAction(action: MainNavAction) {
       _handleActionOservable.trySend(action)
    }
}
