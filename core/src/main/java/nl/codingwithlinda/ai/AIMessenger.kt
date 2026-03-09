package nl.codingwithlinda.ai

import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.core.domain.util.SSResult

interface AIMessenger {
    suspend fun send(message: AIMessage): SSResult<AIMessage, AIError>

    val maxRequestsPerMinute: Int
}

