package nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.presentation.util.permissionsPerBuild
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.concrete_states.BackgroundRunningAllowed
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.concrete_states.BackgroundRunningDenied
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.concrete_states.PermissionNeeded

class SmartStepStateController(
    private val context: ComponentActivity,
) {
    private val _startTracking = MutableStateFlow<StartTrackingState>(PermissionNeeded(context))
    val startTracking = _startTracking.asStateFlow()


    private val permissionLauncher = context.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val allGranted = resultMap.all {
            it.value
        }
        if(!allGranted){
            _startTracking.update {
                PermissionNeeded(context)
            }
        }

    }

    fun onResult(){
        println("--- SMART STEP STATE CONTROLLER --- on result")
        if(!hasPermissions()){
            checkPermissions()
            println("--- SMART STEP STATE CONTROLLER --- has no permissions")
            return
        }
        when(isIgnoringBatteryOptimizations()){
            true -> {
                val state = BackgroundRunningAllowed(context)
                _startTracking.update { state }
            }

            false -> {
                val state = BackgroundRunningDenied(context)
                _startTracking.update { state }
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