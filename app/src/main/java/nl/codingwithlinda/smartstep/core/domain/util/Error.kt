package nl.codingwithlinda.smartstep.core.domain.util

interface Error

data class GeneralError(val message: String): Error
sealed interface FireBaseAIError: Error{
    data class ResourceExhausted(val retryIn: Long): FireBaseAIError
    data object OtherError: FireBaseAIError
}
