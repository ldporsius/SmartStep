package nl.codingwithlinda.smartstep

import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController

class FakeSmartStepStateController: SmartStepStateController {

    override fun onResult() {
        println("fake smart step state controller on result")
    }

    override fun exit() {
        println("fake smart step state controller exit")
    }
}