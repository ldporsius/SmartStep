package nl.codingwithlinda.smartstep.tests.ai_integration

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import kotlin.time.Duration.Companion.seconds


fun fakeAIUserMessages() = List(5){
    AIMessage(
        message = "message $it",
        origin = AIMessageOrigin.USER
    )
}
fun fakeAIAssistantMessages() = List(5){
    AIMessage(
        message = """
            You’re on track today. Keep the pace steady.
            You’re on track today. Keep the pace steady.
            You’re on track today. Keep the pace steady.
            You’re on track today. Keep the pace steady.
            You’re on track today. Keep the pace steady.
            You’re on track today. Keep the pace steady.
        """.trimIndent(),
        origin = AIMessageOrigin.ASSISTANT
    )
}

fun fakeChatHistory() = fakeAIUserMessages().mapIndexed { index, message ->
    listOf(message,fakeAIAssistantMessages()[index])
}.flatten()


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
        val response =   AIMessage(
            message = responses.random(),
            origin = AIMessageOrigin.ASSISTANT
        )
        _messages.update {
            listOf( response )
        }
        return Result.Success(
          response
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