package nl.codingwithlinda.ai.data.finite_state

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai.domain.finite_state.AIState
import nl.codingwithlinda.ai.domain.local_cache.AIChatRepo
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin
import nl.codingwithlinda.ai.domain.model.AIMessenger
import nl.codingwithlinda.core.domain.util.Result
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
            val inWholeMinutes = requestMinuteCount.mapNotNull { timestamp ->
                LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC).takeIf { it.isBefore(now) }
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
                return Result.Failure(
                    AIError.ResourceExhausted(0L),
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

                }
            }
            return result
        }
    }
}