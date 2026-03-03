package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger

class AIChatViewModel(
    private val aiMessenger: AIMessenger
): ViewModel() {

    val chats = aiMessenger.messages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendMessage(msg: String){
        viewModelScope.launch {
            aiMessenger.chat(msg)
        }
    }

}