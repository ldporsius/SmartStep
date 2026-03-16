package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state

sealed interface AIChatAction {
    data class ChatInput(val message: String) : AIChatAction
    data class SendMessage(val message: String) : AIChatAction

}
