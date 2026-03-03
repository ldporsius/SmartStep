package nl.codingwithlinda.smartstep.features.ai_integration.data

import com.google.firebase.ai.type.FirebaseAIException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.domain.util.SSResult
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger

class GeminiAIMessenger(
    var geminiGonfig: GeminiGonfig = Gemini_3_Config()
): AIMessenger {

    private val messageHistory = mutableListOf<AIMessage>()

    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())

    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }

    override suspend fun send(message: AIMessage): SSResult<AIMessage, Exception> {

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
            }catch (e: Exception){
                //e.printStackTrace()

                _messages.update {
                    listOf(
                        AIMessage(e.message ?: "Something went wrong",
                            origin = AIMessageOrigin.ASSISTANT)
                    )
                }

                Result.Failure(e)
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