package nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state

import kotlinx.coroutines.flow.StateFlow

interface SmartStepStateController {
    val state: StateFlow<StartTrackingState>
    fun checkState()
    fun exit()
}