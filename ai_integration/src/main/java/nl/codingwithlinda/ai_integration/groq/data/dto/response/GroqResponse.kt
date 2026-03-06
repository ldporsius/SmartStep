package nl.codingwithlinda.ai_integration.groq.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class GroqResponse(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val service_tier: String,
    val system_fingerprint: String,
    val usage: Usage,
    val usage_breakdown: String? = null,
    val x_groq: XGroq
)