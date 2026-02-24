package nl.codingwithlinda.smartstep.features.main.domain.concrete_states

import android.app.Activity
import android.content.Intent
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.features.main.domain.StartTrackingState

class BackgroundRunningDenied(
    private val activity: Activity,
    private val stepTracker: StepTracker
): StartTrackingState {
    override fun startTracking() {
        println("Background running denied")
        val trackerIntent = Intent(activity, StepTrackerService::class.java).apply {
            action = StepTrackerService.ACTION_STOP
        }
        activity.startService(trackerIntent)
        stepTracker.start()
    }
}