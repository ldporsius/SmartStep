package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.step_tracker.StepTrackerState
import nl.codingwithlinda.smartstep.core.domain.repo.ActivityRecognitionRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

class StepTrackerCounterImpl private constructor(
    context: Context,
    private val repo: ActivityRecognitionRepo,
    private val scope: CoroutineScope
): StepTracker , SensorEventListener {

    private val _stateObservable = MutableStateFlow<StepTrackerState>(StepTrackerState.STOPPED)

    override val stateObservable: StateFlow<StepTrackerState> = _stateObservable

    private val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepCounterSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    companion object{
        @Volatile
        private var instance: StepTracker? = null

        @Synchronized
        fun getInstance(
            context: Context,
            dailyStepRepo: ActivityRecognitionRepo,
            scope: CoroutineScope
        ): StepTracker{
            synchronized(this) {
                val i = instance
                i?.run {
                    return i
                }
                instance = StepTrackerCounterImpl(context,dailyStepRepo, scope)
                return instance!!
            }
        }
    }
    override fun start() {
        synchronized(this) {
            manager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
            _stateObservable.value = StepTrackerState.STARTED
        }
    }

    override fun pause() {
        synchronized(this) {
            //manager.unregisterListener(this)
            _stateObservable.update {
                StepTrackerState.PAUSED
            }
        }
    }

    override fun stop() {
        synchronized(this) {
            manager.unregisterListener(this)
            _stateObservable.update {
                StepTrackerState.STOPPED
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
       //ignore
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null) return

        if (stateObservable.value == StepTrackerState.PAUSED) return

        if (event.sensor?.type == Sensor.TYPE_STEP_COUNTER){

            val deviceLastBootedDate = System.currentTimeMillis() - SystemClock.elapsedRealtime()

            val momentEventTookPlace = deviceLastBootedDate.milliseconds.inWholeNanoseconds + event.timestamp
            val dateOfEvent = LocalDate.ofEpochDay(momentEventTookPlace.nanoseconds.inWholeDays)
            val eventDateYYYYMMDD = DateYYYYMMDD(dateOfEvent.year, dateOfEvent.monthValue, dateOfEvent.dayOfMonth)

            val stepsReceivedFromEvent = event.values[0].toInt()

            println("--- STEP TRACKER COUNTER IMPL --- stepsReceivedFromEvent: $stepsReceivedFromEvent")

            scope.launch {
                 //check if we have a baseline
                val baseline = repo.getDailyStepCountBaselineForDate(eventDateYYYYMMDD)

                if(baseline == null){
                    repo.saveDailyStepCountBaseline(
                        DailyStepCountCreator.create(
                            count = stepsReceivedFromEvent,
                            date = eventDateYYYYMMDD
                        )
                    )
                }
                baseline?.let {baseline ->
                    val difference = stepsReceivedFromEvent - baseline.stepCount
                    val update =  DailyStepCountCreator.create(
                        count = difference,
                        date = eventDateYYYYMMDD
                    )
                    repo.saveStepCount(
                       update
                    )
                }
            }
        }
    }
}