package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker

class StepTrackerImpl(
    private val context: Context,
    private val scope: CoroutineScope
): StepTracker, SensorEventListener{
    private val _stepsTaken = Channel<Int>()
    private var job: Job? = null

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager


    override fun initialize() {
        println("StepTracker initialized")
        val motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        motionSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    override fun start() {

        println("StepTracker started")
    }

    override fun stop() {
       job?.cancel()
    }

    override val stepsTaken: Flow<Int>
        get() = _stepsTaken.receiveAsFlow()

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //todo
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        println("--- onSensorChanged: $p0")

        scope.launch {
           _stepsTaken.send(1)
        }

    }
}