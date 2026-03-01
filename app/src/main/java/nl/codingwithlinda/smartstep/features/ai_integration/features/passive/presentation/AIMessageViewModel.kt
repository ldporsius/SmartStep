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

class AIMessageViewModel(
    private val aiMessenger: AIMessenger
): ViewModel() {

    val emptyMessage = AIMessage(
        message = "empty message",
        origin = AIMessageOrigin.ASSISTANT
    )
    val testMessage = AIMessage(
        message = "What is the sum of 2 and 2",
        origin = AIMessageOrigin.USER
    )
    val testMessageSteps = AIMessage(
        message = "I am tracking my steps. My daily goal is 10000 steps. I have set 200 so far. What would you recommend?",
        origin = AIMessageOrigin.USER
    )
    val latestMessage = aiMessenger.messages.map {
        it.firstOrNull() ?: emptyMessage
    }.onStart {
        aiMessenger.send(
            testMessageSteps
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMessage)


}