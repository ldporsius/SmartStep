package nl.codingwithlinda.smartstep.features.ai_integration.data.local_cache

import kotlinx.serialization.Serializable
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin


@Serializable
data class AIMessageDto(
    val message: String,
    val origin: String
)

fun AIMessageDto.toDomain(): AIMessage{
    return AIMessage(
        message = message,
        origin = AIMessageOrigin.valueOf(this.origin)
    )
}
fun AIMessage.toDto(): AIMessageDto{
    return AIMessageDto(
        message = message,
        origin = origin.name
    )
}