package nl.codingwithlinda.ai.domain.local_cache

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.ai.domain.model.AIMessage

interface AIChatRepo {

    val history: Flow<List<AIMessage>>
    suspend fun saveInHistory(message: AIMessage)
    fun clearHistory()

}