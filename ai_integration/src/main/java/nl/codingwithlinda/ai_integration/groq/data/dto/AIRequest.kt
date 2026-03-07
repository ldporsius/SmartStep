package nl.codingwithlinda.ai_integration.groq.data.dto

import kotlinx.serialization.Serializable
import nl.codingwithlinda.ai_integration.groq.domain.remote.models.llamaScout


@Serializable
data class AIRequest(
    val model: String = llamaScout,
    val messages: List<Message>,
    val temperature: Double = 1.0,
    val max_tokens: Int = 200
)

