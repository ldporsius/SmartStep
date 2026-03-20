package nl.codingwithlinda.smartstep.features.main.statistics.presentation.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.yield
import kotlin.time.Duration.Companion.seconds

class MinuteCounter {

    val minuteCounter = flow {
        while (true){
            emit(System.currentTimeMillis())
            delay(60.seconds)
        }
    }

}