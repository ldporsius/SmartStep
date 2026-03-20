package nl.codingwithlinda.smartstep

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StartTrackingState

class FakeSmartStepStateController: SmartStepStateController {

    val fakeState = object : StartTrackingState {
        override fun startTracking() {
            println("fake smart step state controller start")
        }
    }
    override val state: StateFlow<StartTrackingState>
        get() = MutableStateFlow<StartTrackingState>(fakeState)

    override fun checkState() {
        println("fake smart step state controller on result")
    }

    override fun exit() {
        println("fake smart step state controller exit")
    }
}