package nl.codingwithlinda.ai_firebase.gemini.error

import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.core.domain.util.Error

sealed interface FireBaseAIError: Error {
    data class ResourceExhausted(val retryIn: Long): FireBaseAIError
    data object OtherError: FireBaseAIError
}

fun FireBaseAIError.toDomain(): AIError {
    return when(this){
        is FireBaseAIError.ResourceExhausted -> {
            AIError.ResourceExhausted(this.retryIn)
        }
        is FireBaseAIError.OtherError -> {
            AIError.OtherError
        }
    }
}