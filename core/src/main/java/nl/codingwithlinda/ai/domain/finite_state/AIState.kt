package nl.codingwithlinda.ai.domain.finite_state

import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.core.domain.util.Result

interface AIState {
    suspend fun sendMessage(msg: AIMessage): Result<AIMessage, AIError>
}