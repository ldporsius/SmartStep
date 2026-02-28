package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_STEP_DETECTOR
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log.d
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import nl.codingwithlinda.smartstep.core.domain.repo.ActivityRecognitionRepo
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepGoalCreator
import kotlin.concurrent.Volatile

class StepTrackerDetectorImpl private constructor(
    context: Context,
    private val scope: CoroutineScope,
    private val repo: ActivityRecognitionRepo
): StepTracker, SensorEventListener{

    private var state: StepTrackerState = StepTrackerState.STOPPED

    private val _stateObservable = MutableStateFlow<StepTrackerState>(state)

    override val stateObservable: Flow<StepTrackerState> = _stateObservable

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val motionSensor: Sensor = sensorManager.getSensorList(Sensor.TYPE_STEP_DETECTOR)
        .also {
            println("StepTracker motionSensors detected: $it")
        }
        .firstOrNull() ?: throw Exception("Device has no step detector sensor")


    companion object{
        @Volatile
        private var stepTrackerInstance: StepTrackerDetectorImpl? = null

        val lock = Any()
        @Synchronized
        fun getInstance(
            context: Context,
            scope: CoroutineScope,
            repo: ActivityRecognitionRepo
        ): StepTrackerDetectorImpl {
            synchronized(lock) {
                val i = stepTrackerInstance
                if (i != null) {
                    return i
                }

                stepTrackerInstance = StepTrackerDetectorImpl(context, scope, repo)

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

        motionSensor.let {sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL).also {registered ->
                println("StepTracker registered listener: $registered")
            }
        }
        println("StepTracker started")
    }

    override fun stop() {
        try {
            sensorManager.unregisterListener(this)
        }catch (e: Exception) {
            //ignore
        }

        state = StepTrackerState.STOPPED
        _stateObservable.update {
            StepTrackerState.STOPPED
        }
        println("StepTracker stopped")
    }

    override val stepsTaken: Flow<DailyStepCount> = emptyFlow()

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        println("--- onAccuracyChanged: sensor: $p0, accuracy: $p1")
    }

    override fun onSensorChanged(p0: SensorEvent?) {

        p0?.let {event ->
            if (event.sensor.type == TYPE_STEP_DETECTOR) {

                synchronized(this) {
                    scope.launch {
                        val timestamp = event.timestamp
                        val localDate = SensorTimeStampHelper.timeStampToLocalDate(timestamp)
                        val dateYYYYMMDD = SensorTimeStampHelper.localDateToDomain(localDate)
                        val stepsToday =
                            repo.getStepCountForDate(dateYYYYMMDD.dateEpochDay)?.stepCount ?: 0

                        val update = DailyStepCountCreator.create(stepsToday + 1, dateYYYYMMDD)

                        repo.saveStepCount(update)
                    }
                }
            }
        }
    }
}