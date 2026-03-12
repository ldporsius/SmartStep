package nl.codingwithlinda.ai_firebase.tests

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin
import nl.codingwithlinda.ai_firebase.gemini.chat.GeminiAIChatMessenger
import nl.codingwithlinda.ai_firebase.gemini.passive.Gemini_2_5_Config
import kotlin.time.Duration.Companion.seconds

class FakeAIChatMessenger(
    config: Gemini_2_5_Config,
    dispatcherProvider: DispatcherProvider
): GeminiAIChatMessenger(
    config,dispatcherProvider
) {

    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())

    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }

    override suspend fun chat(text: String) {
        val question = AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
        _messages.update {
            it.toMutableList().plus(question).toList()
        }

        delay(1.seconds)
        val response = fakeAIAssistantMessages().random()
        _messages.update {
            it.toMutableList().plus(response).toList()
        }
    }

    override val messages: Flow <List<AIMessage>> = _messages

}