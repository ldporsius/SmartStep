package nl.codingwithlinda.ai.data.finite_state

import kotlinx.coroutines.flow.firstOrNull
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.ai.AIMessenger
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai.domain.finite_state.AIState

class AIResourceExhaustedState(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo
): AIState {

    override suspend fun sendMessage(msg: AIMessage): Result<AIMessage, AIError> {
        println("--- AI RESOURCE EXHAUSTED STATE --- returning fake")
        val fakeIt = aiSessionRepo.history.firstOrNull()?.random() ?:
        AIMessage(
            "no message",
            AIMessageOrigin.ASSISTANT
        )
        return Result.Success(
            fakeIt
        )
    }
}