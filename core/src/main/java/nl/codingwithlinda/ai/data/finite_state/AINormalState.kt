package nl.codingwithlinda.ai.data.finite_state

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.ai.AIMessenger
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai.domain.finite_state.AIState
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZoneOffset.UTC

class AINormalState(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo,
    private val max_requests_per_minute: Int = 5
): AIState {

    private val mutex = Mutex()

    override suspend fun sendMessage(msg: AIMessage): Result<AIMessage, AIError> {

        mutex.withLock {
            val now = LocalDateTime.now(ZoneOffset.UTC)
            val requestMinuteCount = aiSessionRepo.requestsMadeMinute()
            val inWholeMinutes = requestMinuteCount.map {
                LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC)
            }
            val toText = inWholeMinutes.map {
                "${it.dayOfWeek} ${it.hour}:${it.minute}"

            }

            println("--- AI NORMAL STATE --- now: ${now}")
            println("--- AI NORMAL STATE --- requestMinuteCount: $toText")

            val numRequestsThisMinute = inWholeMinutes.count {
                it.isAfter(now.minusMinutes(1))
            }
            println("--- AI NORMAL STATE --- numRequestsThisMinute: $numRequestsThisMinute")

            val canMakeRequest = numRequestsThisMinute < max_requests_per_minute
            println("--- AI NORMAL STATE --- can make request: $canMakeRequest")

            aiSessionRepo.saveRequestsMadeMinute(now.toEpochSecond(UTC))

            if (!canMakeRequest) {
                val fakeIt =  aiSessionRepo.history.firstOrNull()?.lastOrNull()?.message
                val fakeAppend = fakeIt.run{
                    this.plus("\n\nPlease wait a while before making another request")
                }

                return Result.Failure(
                    AIError.ResourceExhausted(0L),
                    AIMessage(
                        fakeAppend , AIMessageOrigin.ASSISTANT
                    ),
                )
            }


            val result = aiMessenger.send(
                msg
            )
            when (result) {
                is Result.Failure -> {
                    when (result.error) {
                        is AIError.ResourceExhausted -> {
                            aiSessionRepo.saveSessionTimedOut(result.error.retryIn)
                        }
                        is AIError.OtherError -> Unit
                    }
                }

                is Result.Success -> {
                    aiSessionRepo.saveInHistory(result.data)
                }
            }
            return result
        }
    }
}