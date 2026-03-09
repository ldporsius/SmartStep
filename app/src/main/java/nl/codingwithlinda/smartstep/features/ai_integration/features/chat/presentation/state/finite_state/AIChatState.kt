package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.finite_state

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components.AIChatInputOffline
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components.AIChatInputOnline
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatAction

sealed interface AIChatState {
    data class Online(val message: String, val onAction: (AIChatAction) -> Unit): AIChatState
    data object Offline: AIChatState
}


@Composable
fun AIChatState.ToUi(
    modifier: Modifier = Modifier,
){
    when(this){
        AIChatState.Offline -> {
            AIChatInputOffline(
                modifier = modifier
            )
        }
        is AIChatState.Online -> {
            AIChatInputOnline(
                modifier = modifier,
                onAction = onAction,
                message = message
            )
        }
    }
}