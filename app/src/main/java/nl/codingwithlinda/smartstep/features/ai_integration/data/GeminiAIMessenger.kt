package nl.codingwithlinda.smartstep.features.ai_integration.data

import com.google.firebase.Firebase
import com.google.firebase.ai.Chat
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerationConfig
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.ThinkingConfig
import com.google.firebase.ai.type.ThinkingLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.domain.util.SSResult
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger

class GeminiAIMessenger: AIMessenger {

    private val chatHistory = mutableListOf<Content>()
    private val messageHistory = mutableListOf<AIMessage>()

    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())


   /* private val model = com.google.firebase.ai.FirebaseAI.instance.generativeModel(
        modelName = "gemini-2.5-flash"
    )*/

    private val systemInstruction = Content.Builder().setRole(
        "You are a fitness trainer assistant. " +
            "You encourage someone to reach their step count goal. " +
            "You never use more then one sentence." +
        "You speak Dutch"
    ).build()

    private val promptInstructions = StringBuilder()
        .appendLine("You must generate one short textual message - ONE SENTENCE ONLY - that:")
        .appendLine("interprets the current activity state")
        .appendLine("does not contain medical advice")
        .appendLine("does not repeate any values of the users input")
        .appendLine("has a motivational or analytical tone")

    private val promptExamples = StringBuilder()
        .appendLine("You’re on track today. Keep the pace steady.")
        .appendLine("You’re a bit behind your goal — a short walk could help.")
        .appendLine("Great job! You’ve already reached today’s goal.")
        .toString()

    private val completePrompt = StringBuilder()
        .appendLine(promptInstructions.toString())
        .appendLine("Use the following as a guide to generate your response:")
        .appendLine(promptExamples)
        .appendLine("Here is what the user says: ")
    private val generationConfig = GenerationConfig.builder()
        .setThinkingConfig(ThinkingConfig.Builder()
            .setThinkingLevel(ThinkingLevel.LOW)
            .setIncludeThoughts(false)
            .build()
        )
        .build()

    private val model1 = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = "gemini-3-flash-preview",
            generationConfig = generationConfig,
            systemInstruction =  systemInstruction
        )

    private val chat = Chat(
        model = model1,
        history = chatHistory
    )


    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }

    override suspend fun send(message: AIMessage): SSResult<AIMessage, Exception> {
        return withContext(Dispatchers.IO){
            try {
                val prompt = completePrompt.appendLine( message.message).toString()
                println("--- GEMINI AI MESSENGER -- prompt: $prompt")
                val response = model1.generateContent(prompt)
                println("--- GEMINI AI MESSENGER -- response: ${response.text}")
                val msg =  AIMessage(
                    message = response.text ?: "no comment from AI",
                    origin = AIMessageOrigin.ASSISTANT
                )
                messageHistory.add(
                   msg
                )
                _messages.update {
                    messageHistory
                }
                Result.Success(msg)
            }catch (e: Exception){
                e.printStackTrace()
                Result.Failure(e)
            }
        }
    }

    override fun receive(text: String) {
        //todo
    }

    override val messages: Flow<List<AIMessage>>
        get() = _messages.asStateFlow()
}