package nl.codingwithlinda.ai_integration.groq.data

import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.ai.AIMessenger
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai_integration.groq.data.dto.AIRequest
import nl.codingwithlinda.ai_integration.groq.data.dto.Message
import nl.codingwithlinda.ai_integration.groq.data.remote.system_prompts.activityCoachSystemPromptShort
import nl.codingwithlinda.ai_integration.groq.domain.remote.AIService
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.core.domain.util.SSResult

class GroqAIMessenger(
    private val aiService: AIService,
    val systemPrompt: Message = activityCoachSystemPromptShort,
    val maxTokens: Int = 200
): AIMessenger {
    override suspend fun send(message: AIMessage): SSResult<AIMessage, AIError> {
        val request = AIRequest(
            messages = listOf(
                systemPrompt,
                Message(
                    role = "user",
                    content = message.message

                )
            ),
            max_tokens = maxTokens
        )
        val response = aiService.sendMessage(request)

        if (response != null) {
            return Result.Success(
                AIMessage(
                    message = response.response,
                    origin = AIMessageOrigin.ASSISTANT,
                )
            )
        }
        return Result.Failure(AIError.OtherError)
    }
}