package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.tests.ai_integration.FakeAIMessenger

class AIMessageViewModel(
    private val aiMessenger: AIMessenger
): ViewModel() {

    val emptyMessage = AIMessage(
        message = "",
        origin = AIMessageOrigin.ASSISTANT
    )
    val latestMessage = aiMessenger.messages.map {
        it.firstOrNull() ?: emptyMessage
    }.onStart {
        aiMessenger.receive(FakeAIMessenger.responses.random())
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMessage)


}