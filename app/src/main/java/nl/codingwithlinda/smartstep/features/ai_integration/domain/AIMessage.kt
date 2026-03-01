package nl.codingwithlinda.smartstep.features.ai_integration.domain

data class AIMessage(
    val message: String,
    val origin: AIMessageOrigin
)

enum class AIMessageOrigin {
    USER,
    ASSISTANT
}
