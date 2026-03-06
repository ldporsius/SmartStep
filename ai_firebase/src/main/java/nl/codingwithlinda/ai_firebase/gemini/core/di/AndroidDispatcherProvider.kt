package nl.codingwithlinda.ai_firebase.gemini.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import nl.codingwithlinda.core.di.DispatcherProvider

class AndroidDispatcherProvider: DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val mainImmediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}