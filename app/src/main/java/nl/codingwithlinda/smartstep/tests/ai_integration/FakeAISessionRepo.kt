package nl.codingwithlinda.smartstep.tests.ai_integration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.codingwithlinda.smartstep.core.domain.repo.AISessionRepo

class FakeAISessionRepo: AISessionRepo {

    private val _history = flow {
        emit(fakeChatHistory().map { it.message })
    }
    override val history: Flow<List<String>>
        = _history

    override suspend fun saveInHistory(message: String) {

    }

    override suspend fun requestsMadeMinute(): List<Long> {
        return (6 downTo 0).map{
            System.currentTimeMillis() - it * 1000 * 60L
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