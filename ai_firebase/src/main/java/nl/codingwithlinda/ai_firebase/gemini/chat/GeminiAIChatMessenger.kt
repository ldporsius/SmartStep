package nl.codingwithlinda.ai_firebase.gemini.chat

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai_firebase.gemini.core.GeminiAIMessenger
import nl.codingwithlinda.ai_firebase.gemini.core.GeminiFlashConfig
abstract class GeminiAIChatMessenger(
    geminiGonfig: GeminiFlashConfig,
    dispatcherProvider: DispatcherProvider
) : GeminiAIMessenger(
    geminiGonfig,dispatcherProvider
){
    abstract fun create(text: String): AIMessage
    abstract suspend fun chat(text: String)
    abstract val messages: Flow<List<AIMessage>>
}