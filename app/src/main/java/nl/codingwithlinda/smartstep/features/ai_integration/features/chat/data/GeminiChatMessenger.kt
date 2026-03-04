package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data

import com.google.firebase.ai.type.FirebaseAIException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.SSResult
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.GeminiFlashConfig
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIChatMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin

class GeminiChatMessenger(
    var geminiGonfig: GeminiFlashConfig
): AIChatMessenger(geminiGonfig) {
    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }

    private val messageHistory = mutableListOf<AIMessage>()
    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())


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

    override suspend fun send(message: AIMessage): SSResult<AIMessage, FireBaseAIError> {
       return super.send(message)
    }

    override val messages: Flow<List<AIMessage>> = _messages
}