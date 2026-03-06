package nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache

import kotlinx.serialization.Serializable
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin


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