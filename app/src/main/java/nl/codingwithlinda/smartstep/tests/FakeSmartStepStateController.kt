package nl.codingwithlinda.smartstep.tests

import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController

class FakeSmartStepStateController: SmartStepStateController {

    override fun onResult() {
        println("fake smart step state controller on result")
    }
}