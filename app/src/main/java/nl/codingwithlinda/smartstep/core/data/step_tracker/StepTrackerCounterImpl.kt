package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.R.string.no
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

class StepTrackerCounterImpl private constructor(
    context: Context
): StepTracker , SensorEventListener {

    private val _stateObservable = MutableStateFlow<StepTrackerState>(StepTrackerState.STOPPED)

    override val stateObservable: Flow<StepTrackerState> = _stateObservable

    private val _stepsTaken = MutableStateFlow(0)

    private val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepEvent = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


    companion object{
        @Volatile
        private var instance: StepTracker? = null

        @Synchronized
        fun getInstance(context: Context): StepTracker{
            synchronized(this) {
                val i = instance
                i?.run {
                    return i
                }
                instance = StepTrackerCounterImpl(context)
                return instance!!
            }
        }
    }
    override fun start() {
        manager.registerListener(this, stepEvent, SensorManager.SENSOR_DELAY_NORMAL)
        _stateObservable.value = StepTrackerState.STARTED
    }

    override fun pause() {
        manager.unregisterListener(this)
        _stateObservable.update {
            StepTrackerState.PAUSED
        }
    }

    override fun stop() {
        //manager.unregisterListener(this)
        _stateObservable.update {
            StepTrackerState.STOPPED
        }
    }

    override val stepsTaken: Flow<Int> = _stepsTaken

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
       //ignore
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null) return
        if (event.sensor?.type == Sensor.TYPE_STEP_COUNTER){
            println("--- STEP TRACKER COUNTER IMPL --- onSensorChanged: ${event.values.toList()}")
            println("--- STEP TRACKER COUNTER IMPL --- timestamp: ${event.timestamp}")
            val nanosSinceBoot = SystemClock.elapsedRealtimeNanos()
            val sensorTime = event.timestamp
            val now = System.currentTimeMillis()
            val momentSensorStarted = now - sensorTime.div(1000 * 1000)
            println("--- STEP TRACKER COUNTER IMPL --- now: $now")
            println("--- STEP TRACKER COUNTER IMPL --- momentSensorStarted: $momentSensorStarted")

            println("--- STEP TRACKER COUNTER IMPL ---nanosSinceBoot= $nanosSinceBoot, sensorTime = ${sensorTime}, diff = ${nanosSinceBoot - sensorTime}")
            val duration = (nanosSinceBoot - sensorTime).nanoseconds
            println("--- STEP TRACKER COUNTER IMPL --- duration: ${duration}")


            println("--- STEP TRACKER COUNTER IMPL --- duration: ${duration.inWholeDays}d ${duration.inWholeHours}h ${duration.inWholeMinutes}m ${duration.inWholeSeconds}s")
            //println("--- STEP TRACKER COUNTER IMPL --- is first event after discontinuity: ${event.firstEventAfterDiscontinuity}")
            _stepsTaken.value = event.values[0].toInt()
        }
    }
}