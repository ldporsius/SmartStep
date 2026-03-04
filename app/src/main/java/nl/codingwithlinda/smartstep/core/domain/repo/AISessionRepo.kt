package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow

interface AISessionRepo {

    val history: Flow<List<String>>
    suspend fun saveInHistory(message: String)

    suspend fun requestsMadeMinute(): List<Long>
    suspend fun saveRequestsMadeMinute(requestTimestampMillis: Long)
    suspend fun requestsMadeDay(): Int
    suspend fun saveRequestsMadeDay(requests: Int)
    suspend fun sessionTimedOut(): Long
    suspend fun saveSessionTimedOut(time: Long)

}