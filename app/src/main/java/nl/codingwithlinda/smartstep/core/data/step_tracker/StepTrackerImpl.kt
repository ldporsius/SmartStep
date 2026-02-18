package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_STEP_DETECTOR
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import kotlin.concurrent.Volatile

class StepTrackerImpl private constructor(
    context: Context,
    private val scope: CoroutineScope,
): StepTracker, SensorEventListener{

    private var state: StepTrackerState = StepTrackerState.STOPPED

    private val _stateObservable = MutableStateFlow<StepTrackerState>(state)

    override val stateObservable: Flow<StepTrackerState> = _stateObservable

    private val _stepsTaken = Channel<Int>()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val motionSensor = sensorManager.getSensorList(Sensor.TYPE_STEP_DETECTOR)
        .also {
            println("StepTracker motionSensors detected: $it")
        }
        .firstOrNull {
            it.isWakeUpSensor
        }

    companion object{
        @Volatile
        private var stepTrackerInstance: StepTrackerImpl? = null

        val lock = Any()
        @Synchronized
        fun getInstance(
            context: Context,
            scope: CoroutineScope,
        ): StepTrackerImpl {
            synchronized(lock) {
                val i = stepTrackerInstance
                if (i != null) {
                    return i
                }

                stepTrackerInstance = StepTrackerImpl(context, scope)
                return stepTrackerInstance!!

            }
        }
    }

    override fun pause() {
        sensorManager.unregisterListener(this)
        state = StepTrackerState.PAUSED
        _stateObservable.update {
            StepTrackerState.PAUSED
        }
    }
    override fun start() {
        state = StepTrackerState.STARTED
        _stateObservable.update {
            StepTrackerState.STARTED
        }
        println("StepTracker started")
        motionSensor?.let {sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL).also {registered ->
                println("StepTracker registered listener: $registered")
            }
        }
    }

    override fun stop() {
        sensorManager.unregisterListener(this)
        state = StepTrackerState.STOPPED
        _stateObservable.update {
            StepTrackerState.STOPPED
        }
        println("StepTracker stopped")
    }

    override val stepsTaken: Flow<Int>
            = _stepsTaken.receiveAsFlow()

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        println("--- onAccuracyChanged: sensor: $p0, accuracy: $p1")
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        println("--- onSensorChanged: ${p0?.sensor}, values: ${p0?.values?.toList()}")

        p0?.let {event ->
            if (event.sensor.type == TYPE_STEP_DETECTOR)
                println("--- event is step detector. ${event.values.toList()}")
                scope.launch {
                    _stepsTaken.send(1)
                }
        }


    }
}