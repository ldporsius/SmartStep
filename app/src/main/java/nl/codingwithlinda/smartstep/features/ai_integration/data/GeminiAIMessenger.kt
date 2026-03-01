package nl.codingwithlinda.smartstep.features.ai_integration.data

import com.google.firebase.Firebase
import com.google.firebase.ai.Chat
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger

class GeminiAIMessenger: AIMessenger {

    private val chatHistory = mutableListOf<Content>()
    private val messageHistory = mutableListOf<AIMessage>()

    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())


    private val model = com.google.firebase.ai.FirebaseAI.instance.generativeModel(
        modelName = "gemini-2.5-flash"
    )

    private val model1 = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-3-flash-preview")

    private val chat = Chat(
        model = model,
        history = chatHistory
    )


    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }

    override suspend fun send(message: AIMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = model1.generateContent(message.message)
                println("--- GEMINI AI MESSENGER -- response: ${response.text}")
                messageHistory.add(
                    AIMessage(
                        message = response.text ?: "no comment from AI",
                        origin = AIMessageOrigin.ASSISTANT
                    )
                )
                _messages.update {
                    messageHistory
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    override fun receive(text: String) {
        //todo
    }

    /* override val messages: Flow<List<AIMessage>>
         = flow {
             emit(messageHistory)
         }*/
    override val messages: Flow<List<AIMessage>>
        get() = _messages.asStateFlow()
}