package nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state

import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage

interface AIState {
    suspend fun sendMessage(msg: AIMessage): Result<AIMessage, FireBaseAIError>
}