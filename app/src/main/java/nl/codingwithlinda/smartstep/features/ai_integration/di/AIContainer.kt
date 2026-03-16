package nl.codingwithlinda.smartstep.features.ai_integration.di

import android.content.Context
import nl.codingwithlinda.ai.domain.model.AIMessenger
import nl.codingwithlinda.ai.domain.plugin_provider.AIapi
import nl.codingwithlinda.ai.domain.plugin_provider.AImode
import nl.codingwithlinda.ai_firebase.plugin_provider.FirebasePluginProvider
import nl.codingwithlinda.ai_integration.plugin_provider.GroqPluginProvider
import nl.codingwithlinda.smartstep.application.dataStoreAI
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.smartstep.features.ai_integration.data.local_cache.AIChatRepoImpl
import nl.codingwithlinda.smartstep.features.ai_integration.data.local_cache.AISessionRepoImpl

class AIContainer(
    context: Context,
) {
    private val fb =  FirebasePluginProvider()
    private val groq = GroqPluginProvider()

    fun getAIMessenger(mode: AImode, api: AIapi): AIMessenger {
        return when (api) {
            AIapi.GEMINI -> fb.getAIMessenger(mode)
            AIapi.GROQ -> groq.getAIMessenger(mode)
        }
    }
    private val aiSessionRepo = AISessionRepoImpl(
        context.dataStoreAI
    )
    private val aiChatRepoActive = AIChatRepoImpl()
    private val aiChatRepoPassive = AIChatRepoImpl()

    fun getAIStateController(mode: AImode, api: AIapi): AIStateController {
        val aiChatRepo = when(mode){
            AImode.ACTIVE -> aiChatRepoActive
            AImode.PASSIVE -> aiChatRepoPassive
        }
        return AIStateController(
            aiMessenger = getAIMessenger(mode, api),
            aiSessionRepo = aiSessionRepo,
            aiChatRepo = aiChatRepo
        )
    }


    val aiStateControllerFirebase = getAIStateController(
        mode = AImode.ACTIVE,
        api = AIapi.GEMINI
    )

    val aiStateControllerGroqPassive = getAIStateController(
        mode = AImode.PASSIVE,
        api = AIapi.GROQ
    )

    val aiStateControllerGroqActive = getAIStateController(
        mode = AImode.ACTIVE,
        api = AIapi.GROQ
    )

}