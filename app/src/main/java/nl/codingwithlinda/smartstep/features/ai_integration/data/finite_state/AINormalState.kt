package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.AIError
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiAIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.toDto
import kotlin.time.Duration.Companion.seconds

class AINormalState(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo
): AIState {

    private val max_requests_per_minute = 5
    private val mutex = Mutex()


    override suspend fun sendMessage(msg: AIMessage): Result<AIMessage, AIError> {

        mutex.withLock {
            val now = System.currentTimeMillis()
            val requestMinuteCount = aiSessionRepo.requestsMadeMinute()
            println("--- AI NORMAL STATE --- requestMinuteCount: $requestMinuteCount")

            val minutesLeft = requestMinuteCount.count {
                (now - it) < 60.seconds.inWholeMilliseconds
            }
            println("--- AI NORMAL STATE --- Duration: $minutesLeft")

            val canMakeRequest = minutesLeft > 0

            aiSessionRepo.saveRequestsMadeMinute(now)

          /*  if (!canMakeRequest) {
                val fakeIt =  aiSessionRepo.history.firstOrNull()?.lastOrNull() ?: ""
                val fakeAppend = fakeIt.run{
                    this.plus("\n\nPlease wait before making another request")
                }

                return Result.Success(
                    AIMessage(
                      fakeAppend , AIMessageOrigin.ASSISTANT
                )
                )
            }*/

            println("--- AI NORMAL STATE --- hasMaxRequestsInMinute: $canMakeRequest")

            val result = aiMessenger.send(
                msg
            )
            when (result) {
                is Result.Failure -> {
                    when (result.error) {
                        is AIError.ResourceExhausted -> {
                            aiSessionRepo.saveSessionTimedOut(result.error.retryIn)
                        }

                        is AIError.OtherError -> {
                            //todo
                        }
                    }
                }

                is Result.Success -> {
                    aiSessionRepo.saveInHistory(result.data.toDto())
                }
            }
            return result
        }
    }
}