package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.chat

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.application.di.DispatcherProvider
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiFlashConfig
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiAIMessenger

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