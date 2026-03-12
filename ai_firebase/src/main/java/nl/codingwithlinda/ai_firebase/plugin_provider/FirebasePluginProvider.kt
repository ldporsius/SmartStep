package nl.codingwithlinda.ai_firebase.plugin_provider

import nl.codingwithlinda.ai.domain.model.AIMessenger
import nl.codingwithlinda.ai.domain.plugin_provider.AImode
import nl.codingwithlinda.ai_firebase.gemini.chat.GeminiChatMessengerImpl
import nl.codingwithlinda.ai_firebase.gemini.core.GeminiAIMessenger
import nl.codingwithlinda.ai_firebase.gemini.core.di.AndroidDispatcherProvider
import nl.codingwithlinda.ai_firebase.gemini.passive.Gemini_2_5_Config

class FirebasePluginProvider() {

    fun getAIMessenger(mode: AImode): AIMessenger {
        when (mode) {
            AImode.PASSIVE -> {
                return passive.first()
            }
            AImode.ACTIVE -> {
                return active.first()
            }
        }
    }

    private val passive = listOf(
        GeminiAIMessenger(
            geminiGonfig = Gemini_2_5_Config(),
            dispatcherProvider = AndroidDispatcherProvider()
        )
    )

    private val active = listOf(
        GeminiChatMessengerImpl(
            geminiGonfig = Gemini_2_5_Config(),
            dispatcherProvider = AndroidDispatcherProvider()
        )
    )

}