package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import nl.codingwithlinda.smartstep.core.domain.repo.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState

class AINormalState(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo
): AIState {

    override suspend fun sendMessage(msg: AIMessage): Result<AIMessage, FireBaseAIError> {
        val result = aiMessenger.send(
           msg
        )
        when(result){
            is Result.Failure -> {
                when (result.error) {
                    is FireBaseAIError.ResourceExhausted -> {
                        aiSessionRepo.saveSessionTimedOut(result.error.retryIn)
                    }
                    is FireBaseAIError.OtherError -> {
                        //todo
                    }
                }
            }
            is Result.Success -> {
                aiSessionRepo.saveInHistory(result.data.message)
            }
        }
        return result
    }
}