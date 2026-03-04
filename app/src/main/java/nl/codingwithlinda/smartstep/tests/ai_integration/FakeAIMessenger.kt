package nl.codingwithlinda.smartstep.tests.ai_integration

import com.google.firebase.ai.GenerativeModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.GeminiFlashConfig
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.GeminiGonfig
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.Gemini_2_5_Config
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

class FakeAIMessenger: AIMessenger(
    Gemini_2_5_Config()
) {

    companion object {
        val responses = listOf(
            "You’re on track today. Keep the pace steady.",
            "You’re a bit behind your goal — a short walk could help.",
            "Great job! You’ve already reached today’s goal."
        )
    }

    override suspend fun send(message: AIMessage): Result<AIMessage, FireBaseAIError> {
        val response =   AIMessage(
            message = responses.random(),
            origin = AIMessageOrigin.ASSISTANT
        )
        return Result.Success(
          response
        )
    }



}