package nl.codingwithlinda.ai_firebase.tests

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import java.time.LocalDateTime
import java.time.ZoneOffset

class FakeAISessionRepo: AISessionRepo {

    override suspend fun requestsMadeMinute(): List<Long> {
        return (10 downTo 0).map{
            LocalDateTime.now(ZoneOffset.UTC).minusSeconds(it.toLong() * 10).toEpochSecond(ZoneOffset.UTC)
        }
    }

    override suspend fun saveRequestsMadeMinute(requestTimestampMillis: Long) {

    }

    override suspend fun requestsMadeDay(): Int {
        return 0
    }

    override suspend fun saveRequestsMadeDay(requests: Int) {

    }

    override suspend fun sessionTimedOut(): Long {
        return 0L
    }

    override suspend fun saveSessionTimedOut(time: Long) {

    }
}