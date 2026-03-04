package nl.codingwithlinda.smartstep.tests.ai_integration

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.GeminiFlashConfig
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.Gemini_2_5_Config
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIChatMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import kotlin.collections.plus
import kotlin.collections.toList
import kotlin.time.Duration.Companion.seconds

class FakeAIChatMessenger(
   config: GeminiFlashConfig = Gemini_2_5_Config()
): AIChatMessenger(config) {

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