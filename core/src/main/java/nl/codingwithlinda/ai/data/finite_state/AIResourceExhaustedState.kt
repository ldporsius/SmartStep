package nl.codingwithlinda.ai.data.finite_state

import kotlinx.coroutines.flow.firstOrNull
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.ai.AIMessenger
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai.domain.finite_state.AIState
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

class AIResourceExhaustedState(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo
): AIState {

    override suspend fun sendMessage(msg: AIMessage): Result<AIMessage, AIError> {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val requestMinuteCount = aiSessionRepo.requestsMadeMinute()

        val lastRequestMinute = requestMinuteCount.lastOrNull()?: now.toEpochSecond(ZoneOffset.UTC)
        val canTryAgain = lastRequestMinute.let { lastRequest ->
            (now.toEpochSecond(ZoneOffset.UTC) - lastRequest) > 1.minutes.inWholeSeconds
        }

        if (canTryAgain){
            return aiMessenger.send(msg)
        }
        println("--- AI RESOURCE EXHAUSTED STATE --- returning fake")
        val fakeIt = aiSessionRepo.history.firstOrNull()?.random() ?:
        AIMessage(
            "no message",
            AIMessageOrigin.ASSISTANT
        )
        return Result.Failure(
            AIError.ResourceExhausted(lastRequestMinute),
            fakeIt
        )
    }
}