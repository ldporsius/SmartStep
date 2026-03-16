package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.core.domain.util.SSResult
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin
import nl.codingwithlinda.ai.domain.model.AIMessenger
import nl.codingwithlinda.ai.data.finite_state.AINormalState
import nl.codingwithlinda.ai.data.finite_state.AIResourceExhaustedState
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai.domain.finite_state.AIState
import nl.codingwithlinda.ai.domain.local_cache.AIChatRepo

class AIStateController(
    val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo,
    val aiChatRepo: AIChatRepo
) {

    private val aiNormalState = AINormalState(
        aiMessenger = aiMessenger,
        aiSessionRepo = aiSessionRepo,
        max_requests_per_minute = aiMessenger.maxRequestsPerMinute
    )
    private val aiResourceExhaustedState = AIResourceExhaustedState(
        aiMessenger = aiMessenger,
        aiSessionRepo = aiSessionRepo,
        aiChatRepo = aiChatRepo
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
                when(result.error){
                    is AIError.ResourceExhausted -> {
                        aiState.update {
                            aiResourceExhaustedState
                        }
                    }
                }
            }
            is Result.Success -> {
                aiState.update {
                    aiNormalState
                }
                aiChatRepo.saveInHistory(result.data)
            }
        }
        return result
    }

}