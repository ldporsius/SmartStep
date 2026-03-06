package nl.codingwithlinda.smartstep.features.ai_integration.domain

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.util.AIError
import nl.codingwithlinda.smartstep.core.domain.util.SSResult

interface AIMessenger {
    suspend fun send(message: AIMessage): SSResult<AIMessage, AIError>
}

