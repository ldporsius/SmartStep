package nl.codingwithlinda.smartstep.features.main.domain.concrete_states

import nl.codingwithlinda.smartstep.features.main.domain.StartTrackingState

class PermissionNeeded: StartTrackingState {
    override fun startTracking() {
        println("Permission needed")
    }
}
