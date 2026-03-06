package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.AIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.domain.util.SSResult
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState

class AIStateController(
    val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo
) {

    private val aiNormalState = AINormalState(
        aiMessenger = aiMessenger,
        aiSessionRepo = aiSessionRepo
    )
    private val aiResourceExhaustedState = AIResourceExhaustedState(
        aiMessenger = aiMessenger,
        aiSessionRepo = aiSessionRepo
    )

    private val aiState = MutableStateFlow<AIState>(aiNormalState)

    suspend fun sendMessage(msg: String): SSResult<AIMessage, AIError> {
        val msg = AIMessage(
            message = msg,
            origin = AIMessageOrigin.USER
        )
        return sendMessage(msg)
    }
    suspend fun sendMessage(msg: AIMessage): SSResult<AIMessage, AIError>{
        val result = aiState.value.sendMessage(msg)
        when(result){
            is Result.Failure -> {
                aiState.update {
                    aiResourceExhaustedState
                }
            }
            is Result.Success -> {
                aiState.update {
                    aiNormalState
                }
            }
        }
        return result
    }

}