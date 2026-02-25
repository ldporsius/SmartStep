package nl.codingwithlinda.smartstep.features.batteryOptimisation.domain.concrete_states

import android.app.Activity
import android.app.Application
import android.content.Intent
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.stepTracker
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.features.batteryOptimisation.domain.StartTrackingState

class BackgroundRunningDenied(
    private val activity: Application,
): StartTrackingState {
    override fun startTracking() {
        println("Background running denied")
        val trackerIntent = Intent(activity, StepTrackerService::class.java).apply {
            action = StepTrackerService.ACTION_STOP
        }
        activity.startService(trackerIntent)

    }
}