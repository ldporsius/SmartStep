package nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage

interface AISessionRepo {

    val history: Flow<List<AIMessageDto>>
    suspend fun saveInHistory(message: AIMessageDto)

    suspend fun requestsMadeMinute(): List<Long>
    suspend fun saveRequestsMadeMinute(requestTimestampMillis: Long)
    suspend fun requestsMadeDay(): Int
    suspend fun saveRequestsMadeDay(requests: Int)
    suspend fun sessionTimedOut(): Long
    suspend fun saveSessionTimedOut(time: Long)

}