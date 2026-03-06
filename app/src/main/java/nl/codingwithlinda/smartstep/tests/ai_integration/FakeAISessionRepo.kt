package nl.codingwithlinda.smartstep.tests.ai_integration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AIMessageDto
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.toDto

class FakeAISessionRepo: AISessionRepo {

    private val _history = flow {
        emit(fakeChatHistory().map {
            it.toDto()
        })
    }
    override val history: Flow<List<AIMessageDto>> = _history


    override suspend fun saveInHistory(message: AIMessageDto) {

    }


    override suspend fun requestsMadeMinute(): List<Long> {
        return (10 downTo 0).map{
            System.currentTimeMillis() - it * 1000 * 10L
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