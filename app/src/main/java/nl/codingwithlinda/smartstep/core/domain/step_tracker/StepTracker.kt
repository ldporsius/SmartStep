package nl.codingwithlinda.smartstep.core.domain.step_tracker

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.step_tracker.StepTrackerState

interface StepTracker {

    fun start()
    fun pause()
    fun stop()

    val stateObservable: Flow<StepTrackerState>
}