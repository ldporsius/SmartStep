package nl.codingwithlinda.smartstep.features.ai_integration.di

import android.content.Context
import nl.codingwithlinda.ai.domain.plugin_provider.AIapi
import nl.codingwithlinda.ai.domain.plugin_provider.AImode
import nl.codingwithlinda.smartstep.application.dataStoreAI
import nl.codingwithlinda.smartstep.features.ai_integration.data.ai_plugin.AIPluginProvider
import nl.codingwithlinda.smartstep.features.ai_integration.data.local_cache.AISessionRepoImpl

class AIContainer(
    private val context: Context,
) {

    private val aiSessionRepo = AISessionRepoImpl(
        context.dataStoreAI
    )
     val aiPluginProvider = AIPluginProvider(
            aiSessionRepo = aiSessionRepo
        )

    val aiStateControllerFirebase = aiPluginProvider.getAIStateController(
        mode = AImode.ACTIVE,
        api = AIapi.GEMINI
    )

    val aiStateControllerGroq = aiPluginProvider.getAIStateController(
        mode = AImode.ACTIVE,
        api = AIapi.GROQ
    )
}