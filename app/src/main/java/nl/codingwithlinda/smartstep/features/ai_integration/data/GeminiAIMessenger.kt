package nl.codingwithlinda.smartstep.features.ai_integration.data

import com.google.firebase.ai.type.Content
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
    var geminiGonfig: GeminiGonfig = MinimalisticGeminiConfig()
): AIMessenger {

    private val chatHistory = mutableListOf<Content>()
    private val messageHistory = mutableListOf<AIMessage>()

    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())


   /* private val model = com.google.firebase.ai.FirebaseAI.instance.generativeModel(
        modelName = "gemini-2.5-flash"
    )*/




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
                println("--- GEMINI AI MESSENGER -- prompt: $prompt")
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
            messageHistory
        }
        try {
            val prompt = geminiGonfig.promptInstructions() + text
            println("--- GEMINI AI MESSENGER -- prompt: $prompt")
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
                messageHistory
            }
        }catch (e: Exception){
            //e.printStackTrace()
            messageHistory.add(
                AIMessage(
                    message = e.message ?: "Something went wrong",
                    origin = AIMessageOrigin.ASSISTANT
                )
            )
            _messages.update {
                messageHistory
            }
        }
    }

    override val messages: Flow<List<AIMessage>>
        get() = _messages.asStateFlow()
}