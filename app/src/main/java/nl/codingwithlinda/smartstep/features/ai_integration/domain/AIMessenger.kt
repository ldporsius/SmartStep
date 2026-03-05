package nl.codingwithlinda.smartstep.features.ai_integration.domain

import android.util.Log.e
import com.google.firebase.ai.type.FirebaseAIException
import com.google.firebase.ai.type.QuotaExceededException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.domain.util.SSResult
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.GeminiFlashConfig
import kotlin.time.Duration.Companion.milliseconds

abstract class AIMessenger(
    private val geminiGonfig: GeminiFlashConfig
) {
    open suspend fun send(message: AIMessage): SSResult<AIMessage, FireBaseAIError> {

        return withContext(Dispatchers.IO) {
            try {
                val prompt = geminiGonfig.promptInstructions() + message.message

                val response = geminiGonfig.model().generateContent(prompt)
                println("--- GEMINI AI MESSENGER -- response: ${response.text}")
                val msg = AIMessage(
                    message = response.text ?: "no comment from AI",
                    origin = AIMessageOrigin.ASSISTANT
                )

                Result.Success(msg)
            }
            catch (e: QuotaExceededException){
                Result.Failure(FireBaseAIError.ResourceExhausted(0))
            }
            catch (e: FirebaseAIException) {
                e.printStackTrace()

                e.message?.run {
                    Result.Failure(FireBaseAIError.ResourceExhausted(extractRetryTime(this)))
                }
               
                Result.Failure(FireBaseAIError.OtherError)
            }
        }
    }

    private fun extractRetryTime(message: String): Long {
        val retryInSeconds = message.let {
            it.substringAfterLast("Please retry in ").substringBefore("s")
                .toDoubleOrNull() ?: 0.0
        } ?: 0.0
        println("retryInSeconds: $retryInSeconds")

        val retryAtTime =
            System.currentTimeMillis().milliseconds.inWholeSeconds + (retryInSeconds).toLong()

        return retryAtTime
    }


}

abstract class AIChatMessenger(geminiGonfig: GeminiFlashConfig) : AIMessenger(
    geminiGonfig
){
    abstract fun create(text: String): AIMessage
    abstract suspend fun chat(text: String)
    abstract val messages: Flow<List<AIMessage>>
}