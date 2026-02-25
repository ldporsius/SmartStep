package nl.codingwithlinda.smartstep.features.batteryOptimisation.domain.concrete_states

import nl.codingwithlinda.smartstep.features.batteryOptimisation.domain.StartTrackingState

class PermissionNeeded: StartTrackingState {
    override fun startTracking() {
        println("Permission needed")
    }
}
