package nl.codingwithlinda.core.domain.util

typealias SSResult<D,E> = Result<D, E>

sealed interface Result<out D,out E: Error> {
    data class Success<D>(val data: D): Result<D, Nothing>
    class Failure<out E: Error, out D>(val error: E, val data: D? = null): Result<D,E>

}