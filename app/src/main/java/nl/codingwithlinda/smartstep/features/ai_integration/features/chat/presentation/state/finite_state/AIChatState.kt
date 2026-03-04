package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.finite_state

import androidx.compose.runtime.Composable
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components.AIChatInputOffline
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components.AIChatInputOnline
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatAction

sealed interface AIChatState {
    data class Online(val message: String, val onAction: (AIChatAction) -> Unit): AIChatState
    data object Offline: AIChatState
}


@Composable
fun AIChatState.ToUi(){
    when(this){
        AIChatState.Offline -> {
            AIChatInputOffline()
        }
        is AIChatState.Online -> {
            AIChatInputOnline(
                onAction = onAction,
                message = message
            )
        }
    }
}