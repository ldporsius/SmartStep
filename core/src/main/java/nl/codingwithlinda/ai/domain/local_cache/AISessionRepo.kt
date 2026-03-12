package nl.codingwithlinda.ai.domain.local_cache

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.ai.domain.model.AIMessage

interface AISessionRepo {

    val history: Flow<List<AIMessage>>
    suspend fun saveInHistory(message: AIMessage)

    suspend fun requestsMadeMinute(): List<Long>
    suspend fun saveRequestsMadeMinute(requestTimestampMillis: Long)
    suspend fun requestsMadeDay(): Int
    suspend fun saveRequestsMadeDay(requests: Int)
    suspend fun sessionTimedOut(): Long
    suspend fun saveSessionTimedOut(time: Long)

}