package nl.codingwithlinda.ai.domain.error

import nl.codingwithlinda.core.domain.util.Error

interface AIError: Error {
    data class ResourceExhausted(val retryIn: Long): AIError
    data object OtherError: AIError
}
