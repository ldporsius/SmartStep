package nl.codingwithlinda.ai_integration.groq.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class Usage(
    val completion_time: Double,
    val completion_tokens: Int,
    val prompt_time: Double,
    val prompt_tokens: Int,
    val queue_time: Double,
    val total_time: Double,
    val total_tokens: Int
)