package nl.codingwithlinda.smartstep.application.di.ai_plugin

import nl.codingwithlinda.smartstep.application.di.AndroidDispatcherProvider
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.chat.GeminiChatMessengerImpl
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiAIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.passive.Gemini_2_5_Config
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger

class AIPluginProvider(
    private val aiSessionRepo: AISessionRepo
) {

    companion object {
        enum class AImode {
            PASSIVE,
            ACTIVE
        }

        enum class AIapi {
            GEMINI,
            GROQ
        }
    }

    fun getAIStateController(mode: AImode, api: AIapi): AIStateController {
        return AIStateController(
            aiMessenger = getAIMessenger(mode, api),
            aiSessionRepo = aiSessionRepo
        )
    }

    fun getAIMessenger(mode: AImode, api: AIapi): AIMessenger {
        return when (mode) {
            AImode.PASSIVE -> {
                passive.first { it.first == api }.second
            }
            AImode.ACTIVE -> {
                active.first { it.first == api }.second
            }
        }
    }

    val passive = listOf(
        AIapi.GEMINI to GeminiAIMessenger(
            geminiGonfig = Gemini_2_5_Config(),
            dispatcherProvider = AndroidDispatcherProvider()
        )
    )

    val active = listOf(
        AIapi.GEMINI to GeminiChatMessengerImpl(
            geminiGonfig = Gemini_2_5_Config(),
            aiSessionRepo = aiSessionRepo,
            dispatcherProvider = AndroidDispatcherProvider()
        )
    )


}