package nl.codingwithlinda.ai_firebase.tests

import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai_firebase.gemini.core.GeminiAIMessenger
import nl.codingwithlinda.ai_firebase.gemini.passive.Gemini_2_5_Config


fun fakeAIUserMessages() = List(5){
    AIMessage(
        message = "message $it",
        origin = AIMessageOrigin.USER
    )
}
fun fakeAIAssistantMessages() = List(5){
    AIMessage(
        message = """
            1You’re on track today. Keep the pace steady.
            2You’re on track today. Keep the pace steady.
            3You’re on track today. Keep the pace steady.
            4You’re on track today. Keep the pace steady.
            5You’re on track today. Keep the pace steady.
            6You’re on track today. Keep the pace steady.
        """.trimIndent(),
        origin = AIMessageOrigin.ASSISTANT
    )
}

fun fakeChatHistory() = fakeAIUserMessages().mapIndexed { index, message ->
    listOf(message,fakeAIAssistantMessages()[index])
}.flatten()

class FakeAIMessenger(
    dispatcherProvider: DispatcherProvider
): GeminiAIMessenger(
    Gemini_2_5_Config(),
    dispatcherProvider = dispatcherProvider
) {

    companion object {
        val responses = listOf(
            "You’re on track today. Keep the pace steady.",
            "You’re a bit behind your goal — a short walk could help.",
            "Great job! You’ve already reached today’s goal."
        )
    }

    override suspend fun send(message: AIMessage): Result<AIMessage, AIError> {
        val response =   AIMessage(
            message = responses.random(),
            origin = AIMessageOrigin.ASSISTANT
        )
        return Result.Success(
          response
        )
    }
}