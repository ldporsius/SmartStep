package nl.codingwithlinda.ai

data class AIMessage(
    val message: String,
    val origin: AIMessageOrigin
)

enum class AIMessageOrigin {
    USER,
    ASSISTANT
}
