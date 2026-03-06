package nl.codingwithlinda.core.domain.util

typealias SSResult<D,E> = Result<D,E>

sealed interface Result<out D,out E: Error> {
    data class Success<D>(val data: D): Result<D, Nothing>
    data class Failure<E: Error>(val error: E): Result<Nothing,E>

}