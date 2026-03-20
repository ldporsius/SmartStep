package nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.appContainer
import nl.codingwithlinda.smartstep.application.di.AppContainer
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.StartTrackingState
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.concrete_states.BackgroundRunningAllowed
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.concrete_states.BackgroundRunningDenied
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.concrete_states.PermissionNeeded
import nl.codingwithlinda.smartstep.core.domain.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.presentation.util.permissionsPerBuild

class SmartStepStateControllerImpl private constructor(
    private val context: ComponentActivity,
    private val stepTracker: StepTracker,
) : SmartStepStateController {

    companion object{
        fun isIgnoringBattery(context: Context): Boolean{
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return powerManager.isIgnoringBatteryOptimizations(context.packageName)
        }

        @Volatile
        private var instance: SmartStepStateControllerImpl? = null

        @Synchronized
        fun getInstance(
            context: ComponentActivity,
            stepTracker: StepTracker,
        ): SmartStepStateController {
            synchronized(this) {
                if (instance == null) {
                    println("SmartStepStateControllerImpl created")
                    instance = SmartStepStateControllerImpl(
                        context,
                        stepTracker,
                    )
                }
                return instance!!
            }
        }
    }

    private val permNeededState =  PermissionNeeded(
        context,
        emptyMap(),
        stop = {
            stepTracker.stop()
        }
    )
    private val backgroundRunningAllowedState = BackgroundRunningAllowed(
        context,
        start = {
            stepTracker.start()
        }
    )
    private val backgroundRunningDeniedState = BackgroundRunningDenied(
        context,
        start = {
            stepTracker.start()
        }
    )
    private val _state = MutableStateFlow<StartTrackingState>(
       permNeededState
    )

    override val state = _state.asStateFlow()

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



    override fun checkState(){

        if(!hasPermissions()){
            checkPermissions()
            return
        }
        when(isIgnoringBatteryOptimizations()){
            true -> {
                _state.update { backgroundRunningAllowedState }
            }

            false -> {
                _state.update { backgroundRunningDeniedState }
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


}