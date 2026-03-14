package nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.concrete_states

import android.app.Activity
import android.content.Intent
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StartTrackingState

class BackgroundRunningAllowed(
    private val activity: Activity,
    private val start: () -> Unit
): StartTrackingState {
    override fun startTracking() {
        println("Background running allowed")

        val trackerIntent = Intent(activity, StepTrackerService::class.java).apply {
            action = StepTrackerService.ACTION_START
        }
        activity.startService(trackerIntent)

        start()

    }
}

