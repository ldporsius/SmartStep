package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import kotlinx.coroutines.flow.Flow

interface StepTracker {

    fun start()

    fun pause()
    fun stop()
    val stepsTaken: Flow<Int>

    val stateObservable: Flow<StepTrackerState>
}