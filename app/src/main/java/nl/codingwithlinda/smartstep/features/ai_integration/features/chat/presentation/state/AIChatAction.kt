package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state

sealed interface AIChatAction {
    object Intro: AIChatAction
    data class ChatInput(val message: String) : AIChatAction
    data class SendMessage(val message: String) : AIChatAction

}
