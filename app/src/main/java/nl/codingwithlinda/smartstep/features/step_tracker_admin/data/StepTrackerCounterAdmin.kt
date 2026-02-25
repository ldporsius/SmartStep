package nl.codingwithlinda.smartstep.features.step_tracker_admin.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StepTrackerCounterAdmin(
    context: Context,
    private val repo: StepTrackerAdminRepo
): SensorEventListener {

    private val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepCounterSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    fun start(){
        manager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
    }
    fun stop(){
        manager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //ignore
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        val timestamp = p0?.timestamp ?: return
        val steps = p0.values[0]

        CoroutineScope(Dispatchers.IO).launch {
            repo.saveHistory(timestamp, steps.toLong())
        }

    }
}