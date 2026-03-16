package nl.codingwithlinda.smartstep.features.ai_integration.data.local_cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.ai.domain.local_cache.AIChatRepo
import nl.codingwithlinda.ai.domain.model.AIMessage

class AIChatRepoImpl: AIChatRepo {

    private val _history = MutableStateFlow<List<AIMessage>>(emptyList())
    override val history: Flow<List<AIMessage>> = _history

    override suspend fun saveInHistory(message: AIMessage) {
        _history.update {
            _history.value.plus(message)
        }
    }

    override fun clearHistory() {
       _history.update {
           emptyList()
       }
    }
}