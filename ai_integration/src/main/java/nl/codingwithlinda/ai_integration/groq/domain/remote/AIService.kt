package nl.codingwithlinda.ai_integration.groq.domain.remote

import nl.codingwithlinda.ai_integration.groq.data.dto.AIResponse
import nl.codingwithlinda.ai_integration.groq.data.dto.AIRequest

interface AIService {

    suspend fun sendMessage(message: AIRequest): AIResponse?

}