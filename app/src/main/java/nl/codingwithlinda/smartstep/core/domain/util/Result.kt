package nl.codingwithlinda.smartstep.core.domain.util

typealias SSResult<D,E> = Result<D,E>

sealed interface Result<out D,out E: Throwable> {
    data class Success<D>(val data: D): Result<D, Nothing>
    data class Failure<E: Throwable>(val error: E): Result<Nothing,E>

}