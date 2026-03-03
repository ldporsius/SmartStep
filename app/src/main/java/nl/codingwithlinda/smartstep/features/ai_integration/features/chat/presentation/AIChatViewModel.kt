package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatAction
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatUiState

class AIChatViewModel(
    private val aiMessenger: AIMessenger
): ViewModel() {

    val chats = aiMessenger.messages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(AIChatUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AIChatAction){
        when(action){
            is AIChatAction.ChatInput -> {
                _uiState.value = _uiState.value.copy(
                    message = action.message
                )
            }
            is AIChatAction.SendMessage -> {
                sendMessage(action.message)
            }
        }
    }


    private fun sendMessage(msg: String){
        println("Sending message to chat: $msg")
        viewModelScope.launch {
            aiMessenger.chat(msg)
        }
    }

}