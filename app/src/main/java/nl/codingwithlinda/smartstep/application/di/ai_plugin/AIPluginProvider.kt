package nl.codingwithlinda.smartstep.application.di.ai_plugin

import nl.codingwithlinda.ai.AIMessenger
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.ai.domain.plugin_provider.AIapi
import nl.codingwithlinda.ai.domain.plugin_provider.AImode
import nl.codingwithlinda.ai_firebase.plugin_provider.FirebasePluginProvider
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController

class AIPluginProvider(
    private val aiSessionRepo: AISessionRepo
) {


    fun getAIStateController(mode: AImode, api: AIapi): AIStateController {
        return AIStateController(
            aiMessenger = getAIMessenger(mode, api),
            aiSessionRepo = aiSessionRepo
        )
    }

    private val fb =  FirebasePluginProvider()

    fun getAIMessenger(mode: AImode, api: AIapi): AIMessenger {
        return when (api) {
            AIapi.GEMINI -> fb.getAIMessenger(mode)
            AIapi.GROQ -> fb.getAIMessenger(mode)
        }
    }
}