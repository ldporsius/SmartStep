package nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state

import kotlinx.coroutines.flow.Flow

interface SmartStepStateController {
    val state: Flow<StartTrackingState>
    fun onResult()
    fun exit()
}