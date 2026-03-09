package nl.codingwithlinda.ai_firebase.gemini.core

import com.google.firebase.ai.type.FirebaseAIException
import com.google.firebase.ai.type.QuotaExceededException
import kotlinx.coroutines.withContext
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.ai.AIMessenger
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai_firebase.gemini.error.FireBaseAIError
import nl.codingwithlinda.ai_firebase.gemini.error.toDomain
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.core.domain.util.SSResult
import kotlin.time.Duration.Companion.milliseconds

open class GeminiAIMessenger(
    private val geminiGonfig: GeminiFlashConfig,
    private val dispatcherProvider: DispatcherProvider
): AIMessenger {

    override val maxRequestsPerMinute: Int
        get() = 5
    override suspend fun send(message: AIMessage): SSResult<AIMessage, AIError> {
        return withContext(dispatcherProvider.io) {
            val result = sendM(message)
            when (result) {
                is Result.Failure -> {
                    Result.Failure(result.error.toDomain())
                }

                is Result.Success -> result
            }
        }
    }
    private suspend fun sendM(message: AIMessage): SSResult<AIMessage, FireBaseAIError> {
          return  try {
                val prompt = geminiGonfig.promptInstructions() + message.message

                val response = geminiGonfig.model().generateContent(prompt)
                println("--- GEMINI AI MESSENGER -- response: ${response.text}")
                val msg = AIMessage(
                    message = response.text ?: "no comment from AI",
                    origin = AIMessageOrigin.ASSISTANT
                )

                Result.Success(msg)
            } catch (e: QuotaExceededException) {
                Result.Failure(FireBaseAIError.ResourceExhausted(0))
            } catch (e: FirebaseAIException) {
                e.printStackTrace()

                e.message?.run {
                    Result.Failure(
                        FireBaseAIError.ResourceExhausted(extractRetryTime(this)),
                        null
                    )
                }

                Result.Failure(FireBaseAIError.OtherError)
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