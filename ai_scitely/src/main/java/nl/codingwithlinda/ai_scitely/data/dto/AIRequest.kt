package nl.codingwithlinda.ai_scitely.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class AIRequest(
    val model: String = "meta-llama/llama-4-scout-17b-16e-instruct",
    val messages: List<Message>,
)

@Serializable
data class Message(
    val role: String,
    val content: String,
)
