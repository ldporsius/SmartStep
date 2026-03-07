package nl.codingwithlinda.ai_integration.plugin_provider

import nl.codingwithlinda.ai.AIMessenger
import nl.codingwithlinda.ai.domain.plugin_provider.AImode
import nl.codingwithlinda.ai_integration.groq.data.GroqAIMessenger
import nl.codingwithlinda.ai_integration.groq.data.remote.AIServiceImpl
import nl.codingwithlinda.ai_integration.groq.data.remote.system_prompts.activityCoachSystemPromptAverage


class GroqPluginProvider() {

    val aiService = AIServiceImpl()
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
        GroqAIMessenger(aiService)
    )

    private val active = listOf(
        GroqAIMessenger(
            aiService = aiService,
            systemPrompt = activityCoachSystemPromptAverage,
            maxTokens = 500
        )
    )

}