package nl.codingwithlinda.ai.domain.model

data class AIMessage(
    val message: String,
    val origin: AIMessageOrigin
)

enum class AIMessageOrigin {
    USER,
    ASSISTANT
}
