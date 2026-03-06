package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import kotlinx.coroutines.flow.firstOrNull
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.AIError
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiAIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AIMessageDto
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.toDomain

class AIResourceExhaustedState(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo
): AIState {

    override suspend fun sendMessage(msg: AIMessage): Result<AIMessage, AIError> {
        println("--- AI RESOURCE EXHAUSTED STATE --- returning fake")
        val fakeIt = aiSessionRepo.history.firstOrNull()?.random() ?:
        AIMessageDto(
            "no message",
            AIMessageOrigin.ASSISTANT.name
        )
        val msg = fakeIt.toDomain()
        return Result.Success(
            msg
        )
    }
}