package nl.codingwithlinda.smartstep.features.ai_integration.domain

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.SSResult

interface AIMessenger {

    fun create(text: String): AIMessage
    suspend fun send(message: AIMessage): SSResult<AIMessage, FireBaseAIError>
    suspend fun chat(text: String)
    val messages: Flow<List<AIMessage>>
}

