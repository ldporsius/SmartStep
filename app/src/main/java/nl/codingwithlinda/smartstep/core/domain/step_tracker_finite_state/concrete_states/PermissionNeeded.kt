package nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.concrete_states


import android.app.Activity
import android.content.Intent
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StartTrackingState
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionUiState
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionActivityRecognitionToUiState

class PermissionNeeded(
    private val context: Activity,
    private val neededPermissions: Map<String, Boolean>,
    private val stop: () -> Unit
): StartTrackingState {

    fun getFirstNeededPermission() = neededPermissions.toList().firstOrNull()

    fun getPermissionUiState(): PermissionUiState{
       return getFirstNeededPermission()?.first?.let {
            context.PermissionActivityRecognitionToUiState(it)
        } ?: PermissionUiState.NA
    }
    override fun startTracking() {
        println("Permission needed")

        val trackerIntent = Intent(context, StepTrackerService::class.java).apply {
            action = StepTrackerService.ACTION_STOP
        }
        context.startService(trackerIntent)
        stop()

    }
}
