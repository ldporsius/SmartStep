package nl.codingwithlinda.smartstep.features.statistics.presentation.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.yield
import kotlin.time.Duration.Companion.seconds

object MinuteCounter {

    private var shouldCount = false
    val minuteCounter = flow {
        while (shouldCount){
            emit(System.currentTimeMillis())
            yield()
            delay(60.seconds)
        }
    }

    fun start(){
        shouldCount = true
    }
    fun stop(){
        shouldCount = false
    }
}