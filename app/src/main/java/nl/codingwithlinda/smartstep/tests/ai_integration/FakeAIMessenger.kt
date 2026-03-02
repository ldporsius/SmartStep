package nl.codingwithlinda.smartstep.tests.ai_integration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger

class FakeAIMessenger: AIMessenger {

    companion object {
        val responses = listOf(
            "You’re on track today. Keep the pace steady.",
            "You’re a bit behind your goal — a short walk could help.",
            "Great job! You’ve already reached today’s goal."
        )
    }
    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())

    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }
    override suspend fun send(message: AIMessage): Result<AIMessage, Exception> {
        _messages.update {
            it + message
        }
        return Result.Success(
            AIMessage(
                message = responses.random(),
                origin = AIMessageOrigin.ASSISTANT
            )
        )
    }

    override fun receive(text: String) {
       val response = AIMessage(
            message = text,
            origin = AIMessageOrigin.ASSISTANT
        )
        _messages.update {
            it + response
        }
    }

    override val messages: Flow <List<AIMessage>> = _messages

}