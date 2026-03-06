package nl.codingwithlinda.ai_integration.groq.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class XGroq(
    val id: String,
    val seed: Int
)