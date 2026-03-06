package nl.codingwithlinda.smartstep.core.domain.util

interface Error

data class GeneralError(val message: String): Error


sealed interface AIError: Error{
    data class ResourceExhausted(val retryIn: Long): AIError
    data object OtherError: AIError
}
sealed interface FireBaseAIError: Error{
    data class ResourceExhausted(val retryIn: Long): FireBaseAIError
    data object OtherError: FireBaseAIError
}

fun FireBaseAIError.toDomain(): AIError{
    return when(this){
        is FireBaseAIError.ResourceExhausted -> {
            AIError.ResourceExhausted(this.retryIn)
            }
        is FireBaseAIError.OtherError -> {
            AIError.OtherError
        }
    }
}
