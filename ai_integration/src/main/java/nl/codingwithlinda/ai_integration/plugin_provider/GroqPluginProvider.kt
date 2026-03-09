package nl.codingwithlinda.ai_integration.plugin_provider

import nl.codingwithlinda.ai.AIMessenger
import nl.codingwithlinda.ai.domain.plugin_provider.AImode
import nl.codingwithlinda.ai_integration.groq.data.GroqAIMessenger
import nl.codingwithlinda.ai_integration.groq.data.remote.AIServiceImpl
import nl.codingwithlinda.ai_integration.groq.data.remote.system_prompts.activityCoachSystemPromptAverage
import nl.codingwithlinda.ai_integration.groq.data.remote.system_prompts.activityCoachSystemPromptShort
import java.util.Locale


class GroqPluginProvider() {

    val aiService = AIServiceImpl()
    fun getAIMessenger(mode: AImode): AIMessenger {
        return when (mode) {
            AImode.PASSIVE -> {
                passive
            }

            AImode.ACTIVE -> {
                active
            }
        }
    }

    private val passive =
        GroqAIMessenger(
            aiService = aiService,
            systemPrompt = activityCoachSystemPromptShort,
            maxTokens = 200,
            temperature = 0.5
        )


    private val active =
        GroqAIMessenger(
            aiService = aiService,
            systemPrompt = activityCoachSystemPromptAverage,
            maxTokens = 500,
            temperature = 1.5,
            language = Locale.getDefault().language
        )


}