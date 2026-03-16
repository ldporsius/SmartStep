package nl.codingwithlinda.ai.domain.local_cache


interface AISessionRepo {
    suspend fun requestsMadeMinute(): List<Long>
    suspend fun saveRequestsMadeMinute(requestTimestampMillis: Long)
    suspend fun requestsMadeDay(): Int
    suspend fun saveRequestsMadeDay(requests: Int)
    suspend fun sessionTimedOut(): Long
    suspend fun saveSessionTimedOut(time: Long)

}