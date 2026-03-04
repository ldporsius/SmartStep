package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini

import com.google.firebase.ai.type.FirebaseAIException
import com.google.firebase.ai.type.PublicPreviewAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.domain.util.SSResult
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import kotlin.time.Duration.Companion.milliseconds

@OptIn(PublicPreviewAPI::class)
class GeminiAIMessenger(
    var geminiGonfig: GeminiFlashConfig
): AIMessenger {

    private val messageHistory = mutableListOf<AIMessage>()

    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())

    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }

    override suspend fun send(message: AIMessage): SSResult<AIMessage, FireBaseAIError> {

        return withContext(Dispatchers.IO){
            try {
                val prompt = geminiGonfig.promptInstructions() + message.message

                val response = geminiGonfig.model().generateContent(prompt)
                println("--- GEMINI AI MESSENGER -- response: ${response.text}")
                val msg =  AIMessage(
                    message = response.text ?: "no comment from AI",
                    origin = AIMessageOrigin.ASSISTANT
                )

                _messages.update {
                    listOf(msg)
                }
                Result.Success(msg)
            }catch (e: FirebaseAIException){
                e.printStackTrace()

                if (e.message?.contains("RESOURCE_EXHAUSTED") == true){
                    val retryInSeconds = e.message?.let {
                        it.substringAfterLast("Please retry in ").substringBefore("s")
                            .toDoubleOrNull() ?: 0.0
                    } ?: 0.0
                    println("retryInSeconds: $retryInSeconds")

                    val retryAtTime = System.currentTimeMillis().milliseconds.inWholeSeconds + (retryInSeconds).toLong()

                    System.currentTimeMillis().milliseconds.inWholeSeconds
                    return@withContext Result.Failure(FireBaseAIError.ResourceExhausted(retryAtTime))
                }
                Result.Failure(FireBaseAIError.OtherError)
            }
        }
    }

    override suspend fun chat(text: String) {
        val msg = AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
        messageHistory.add(
            msg
        )
        _messages.update {
            messageHistory.toList()
        }
        try {
            val prompt = geminiGonfig.promptInstructions() + text

            val response = geminiGonfig.model().generateContent(prompt)
            println("--- GEMINI AI MESSENGER -- response: ${response.text}")
            val msgResponse =  AIMessage(
                message = response.text ?: "no comment from AI",
                origin = AIMessageOrigin.ASSISTANT
            )
            messageHistory.add(
                msgResponse
            )
            _messages.update {
                messageHistory.toList()
            }
        }catch (e: FirebaseAIException){
            //e.printStackTrace()
            messageHistory.add(
                AIMessage(
                    message = e.localizedMessage ?: "Something went wrong",
                    origin = AIMessageOrigin.ASSISTANT
                )
            )
            _messages.update {
                messageHistory.toList()
            }
        }
    }

    override val messages: Flow<List<AIMessage>>
        get() = _messages.asStateFlow()
}