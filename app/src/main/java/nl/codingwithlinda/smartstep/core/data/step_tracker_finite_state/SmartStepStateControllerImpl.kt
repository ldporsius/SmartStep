package nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.application.di.AppContainer
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StartTrackingState
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.concrete_states.BackgroundRunningAllowed
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.concrete_states.BackgroundRunningDenied
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.concrete_states.PermissionNeeded
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.presentation.util.permissionsPerBuild

class SmartStepStateControllerImpl(
    private val context: ComponentActivity,
    private val stepTracker: StepTracker
) : SmartStepStateController {
    private val _state = MutableStateFlow<StartTrackingState>(
        PermissionNeeded(
            context,
            emptyMap(),
            stop = {
                stepTracker.stop()
            }
        )
    )
    val startTrackingState = _state.asStateFlow()


    private val permissionLauncher = context.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val allGranted = resultMap.all {
            it.value
        }
        val notGranted = resultMap.filter {
            it.value == false
        }
        if(!allGranted){
            _state.update {
                PermissionNeeded(
                    context = context,
                    neededPermissions = notGranted,
                    stop = {
                        stepTracker.stop()
                    }
                )
            }
        }
    }

    override fun exit() {
        val trackerIntent = Intent(context, StepTrackerService::class.java).apply {
            action = StepTrackerService.Companion.ACTION_STOP
        }
        context.startService(trackerIntent)
        context.finish()
    }
    override fun onResult(){
        println("--- SMART STEP STATE CONTROLLER --- on result")
        if(!hasPermissions()){
            checkPermissions()
            println("--- SMART STEP STATE CONTROLLER --- has no permissions")
            return
        }
        when(isIgnoringBatteryOptimizations()){
            true -> {
                val state = BackgroundRunningAllowed(
                    context,
                    start = {
                        stepTracker.start()
                    }
                )
                _state.update { state }
            }

            false -> {
                val state = BackgroundRunningDenied(context) {
                    stepTracker.start()
                }
                _state.update { state }
            }
        }
    }

    fun isIgnoringBatteryOptimizations(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    private fun hasPermissions(): Boolean {
        val hasPermissions = permissionsPerBuild(Build.VERSION.SDK_INT).map{
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }.all { granted ->
            granted
        }

        return hasPermissions
    }

    private fun checkPermissions(){
        permissionLauncher.launch(permissionsPerBuild(Build.VERSION.SDK_INT).toTypedArray())
    }

    companion object{
        fun isIgnoringBattery(context: Context): Boolean{
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return powerManager.isIgnoringBatteryOptimizations(context.packageName)
        }
    }
}