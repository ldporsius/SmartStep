package nl.codingwithlinda.smartstep.tests.ai_integration

import nl.codingwithlinda.smartstep.application.di.DispatcherProvider
import nl.codingwithlinda.smartstep.core.domain.util.AIError
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.passive.Gemini_2_5_Config
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiAIMessenger


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