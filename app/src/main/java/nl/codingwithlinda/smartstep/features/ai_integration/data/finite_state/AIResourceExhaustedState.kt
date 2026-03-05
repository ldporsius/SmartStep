package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import kotlinx.coroutines.flow.firstOrNull
import nl.codingwithlinda.smartstep.core.domain.repo.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState

class AIResourceExhaustedState(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo
): AIState {

    override suspend fun sendMessage(msg: AIMessage): Result<AIMessage, FireBaseAIError> {
        println("--- AI RESOURCE EXHAUSTED STATE --- returning fake")
        val fakeIt = aiSessionRepo.history.firstOrNull()?.random() ?: "No history"
        return Result.Success(
            AIMessage(
                message = fakeIt,
                origin = AIMessageOrigin.ASSISTANT
            )
        )
    }
}