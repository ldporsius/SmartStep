package nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.concrete_states

import android.app.Activity
import android.content.Intent
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StartTrackingState

class BackgroundRunningDenied(
    private val context: Activity,
): StartTrackingState {
    override fun startTracking() {
        println("Background running denied")
        val trackerIntent = Intent(context, StepTrackerService::class.java).apply {
            action = StepTrackerService.ACTION_STOP
        }
        context.startService(trackerIntent)

        SmartStepApplication.stepTracker.start()

    }
}