package nl.codingwithlinda.ai_integration.groq.data.dto.response

import kotlinx.serialization.Serializable
import nl.codingwithlinda.ai_integration.groq.data.dto.Message

@Serializable
data class Choice(
    val finish_reason: String = "",
    val index: Int = -1,
    val logprobs: String? = null,
    val message: Message
)