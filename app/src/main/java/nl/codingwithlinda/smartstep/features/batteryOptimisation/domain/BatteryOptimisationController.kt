package nl.codingwithlinda.smartstep.features.batteryOptimisation.domain

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.PowerManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import nl.codingwithlinda.smartstep.features.batteryOptimisation.domain.concrete_states.BackgroundRunningAllowed
import nl.codingwithlinda.smartstep.features.batteryOptimisation.domain.concrete_states.BackgroundRunningDenied

class BatteryOptimisationController(
    private val context: Application,
) {
    private val _startTracking = Channel<StartTrackingState>()
    val startTracking = _startTracking.receiveAsFlow()


    fun canRunInBackgroundService(): Boolean = isIgnoringBatteryOptimizations()

    fun onResult(){
        when(isIgnoringBatteryOptimizations()){
            true -> {
                val state = BackgroundRunningAllowed(context)
                _startTracking.trySend(state)
            }

            false -> {
                val state = BackgroundRunningDenied(context)
                _startTracking.trySend(state)
            }
        }
    }

    private fun isIgnoringBatteryOptimizations(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }
}