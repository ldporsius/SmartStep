package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import android.provider.SyncStateContract.Helpers.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.dailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

class StepTrackerCounterImpl private constructor(
    context: Context,
    private val dailyStepRepo: DailyStepRepo
): StepTracker , SensorEventListener {

    private val _stateObservable = MutableStateFlow<StepTrackerState>(StepTrackerState.STOPPED)

    override val stateObservable: StateFlow<StepTrackerState> = _stateObservable

    private val _stepsTaken = MutableStateFlow(DailyStepCount(0,0,0,0))

    private val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepCounterSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    companion object{
        @Volatile
        private var instance: StepTracker? = null

        @Synchronized
        fun getInstance(
            context: Context,
            dailyStepRepo: DailyStepRepo
        ): StepTracker{
            synchronized(this) {
                val i = instance
                i?.run {
                    return i
                }
                instance = StepTrackerCounterImpl(context,dailyStepRepo)
                return instance!!
            }
        }
    }
    override fun start() {
        manager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
        _stateObservable.value = StepTrackerState.STARTED
    }

    override fun pause() {
        manager.unregisterListener(this)
        _stateObservable.update {
            StepTrackerState.PAUSED
        }
    }

    override fun stop() {
        manager.unregisterListener(this)
        _stateObservable.update {
            StepTrackerState.STOPPED
        }
    }

    override val stepsTaken: Flow<DailyStepCount> = _stepsTaken

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
       //ignore
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null) return

        if (stateObservable.value == StepTrackerState.PAUSED) return

        if (event.sensor?.type == Sensor.TYPE_STEP_COUNTER){
            //println("--- STEP TRACKER COUNTER IMPL --- onSensorChanged: ${event.values.toList()}")
            //println("--- STEP TRACKER COUNTER IMPL --- timestamp: ${event.timestamp}")


            val deviceLastBootedDate = System.currentTimeMillis() - SystemClock.elapsedRealtime()
            //println("--- STEP TRACKER COUNTER IMPL --- deviceLastBootedDate: ${LocalDate.ofEpochDay(deviceLastBootedDate.milliseconds.inWholeDays)}")
            //println("--- STEP TRACKER COUNTER IMPL --- deviceLastBootedInWholeMillis: ${deviceLastBootedDate.milliseconds.inWholeMilliseconds}")

            val momentEventTookPlace = deviceLastBootedDate.milliseconds.inWholeNanoseconds + event.timestamp
            //println("--- STEP TRACKER COUNTER IMPL --- momentEventTookPlace: ${momentEventTookPlace}")
            val dateOfEvent = LocalDate.ofEpochDay(momentEventTookPlace.nanoseconds.inWholeDays)
            //val dateTimeOfEvent = LocalDateTime.ofEpochSecond(momentEventTookPlace.nanoseconds.inWholeSeconds, 0, java.time.ZoneOffset.UTC)
            //println("--- STEP TRACKER COUNTER IMPL --- dateTimeOfEvent: $dateTimeOfEvent")

            val stepsReceivedFromEvent = event.values[0].toInt()

            println("--- STEP TRACKER COUNTER IMPL --- stepsReceivedFromEvent: $stepsReceivedFromEvent")

            CoroutineScope(Dispatchers.IO).launch {
                val eventDateYYYYMMDD = DateYYYYMMDD(dateOfEvent.year, dateOfEvent.monthValue, dateOfEvent.dayOfMonth)
                //check if we have a baseline
                val baseline = dailyStepRepo.getDailyStepCountBaselineForDate(eventDateYYYYMMDD)

                if(baseline == null){
                    dailyStepRepo.saveDailyStepCountBaseline(
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
                    dailyStepRepo.saveStepCount(
                       update
                    )
                    _stepsTaken.update {
                        update
                    }
                }
            }
        }
    }
}