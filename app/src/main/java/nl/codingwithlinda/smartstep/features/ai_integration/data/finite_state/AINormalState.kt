package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.codingwithlinda.smartstep.core.domain.repo.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState
import kotlin.time.Duration.Companion.milliseconds

class AINormalState(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo
): AIState {

    private val max_requests_per_minute = 5
    private val mutex = Mutex()


    override suspend fun sendMessage(msg: AIMessage): Result<AIMessage, FireBaseAIError> {

        mutex.withLock {
            val now = System.currentTimeMillis()
            val requestMinuteCount = aiSessionRepo.requestsMadeMinute()
            println("--- AI NORMAL STATE --- requestMinuteCount: $requestMinuteCount")

            val durationsMinutes = requestMinuteCount.chunked(6).map {
                it.last() - it.first()
            }.map {
                it.milliseconds.inWholeMinutes
            }
            println("--- AI NORMAL STATE --- Duration: $durationsMinutes")

            val isWithinMinute = (durationsMinutes.lastOrNull() ?: 0) < max_requests_per_minute

            aiSessionRepo.saveRequestsMadeMinute(now)

            if (!isWithinMinute) {
                val fakeIt =  aiSessionRepo.history.firstOrNull()?.lastOrNull() ?: ""
                val fakeAppend = fakeIt.run{
                    this.plus("\nPlease wait before making another request")
                }

                return Result.Success(
                    AIMessage(
                      fakeAppend ,
                        AIMessageOrigin.ASSISTANT
                )
                )
            }

            println("--- AI NORMAL STATE --- isWithinMinute: $isWithinMinute")

            val result = aiMessenger.send(
                msg
            )


            when (result) {
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
}