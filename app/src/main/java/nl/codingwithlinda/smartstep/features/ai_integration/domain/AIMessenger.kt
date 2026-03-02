package nl.codingwithlinda.smartstep.features.ai_integration.domain

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.util.Result

interface AIMessenger {

    fun create(text: String): AIMessage
    suspend fun send(message: AIMessage): Result<AIMessage, Exception>
    fun receive(text: String)
    val messages: Flow<List<AIMessage>>
}

