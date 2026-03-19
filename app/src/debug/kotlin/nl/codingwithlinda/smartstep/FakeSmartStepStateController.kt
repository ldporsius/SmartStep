package nl.codingwithlinda.smartstep

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StartTrackingState

class FakeSmartStepStateController: SmartStepStateController {
    override val state: Flow<StartTrackingState>
        get() = flowOf()

    override fun onResult() {
        println("fake smart step state controller on result")
    }

    override fun exit() {
        println("fake smart step state controller exit")
    }
}