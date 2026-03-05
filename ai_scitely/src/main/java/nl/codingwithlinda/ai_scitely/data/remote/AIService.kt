package nl.codingwithlinda.ai_scitely.data.remote

import nl.codingwithlinda.ai_scitely.data.dto.AIRequest
import nl.codingwithlinda.ai_scitely.data.dto.AIResponse

interface AIService {

    suspend fun sendMessage(message: AIRequest): AIResponse?

}