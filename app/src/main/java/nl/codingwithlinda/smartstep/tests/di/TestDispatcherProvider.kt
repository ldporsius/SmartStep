package nl.codingwithlinda.smartstep.tests.di

import kotlinx.coroutines.CoroutineDispatcher
import nl.codingwithlinda.core.di.DispatcherProvider

class TestDispatcherProvider(
    var testDispatcher: CoroutineDispatcher
): DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val mainImmediate: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher

}