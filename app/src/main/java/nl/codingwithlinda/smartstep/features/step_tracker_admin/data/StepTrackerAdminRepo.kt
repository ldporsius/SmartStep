package nl.codingwithlinda.smartstep.features.step_tracker_admin.data

import android.os.SystemClock
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SensorStepCounterDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.SensorStepCounterData
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

class StepTrackerAdminRepo(
    val dao: SensorStepCounterDao
) {

    suspend fun saveHistory(timestampSinceBootNanos: Long, steps: Long){

        val millisSinceLastBoot = System.currentTimeMillis() - SystemClock.elapsedRealtime()
        val dateLastBooted = LocalDate.ofEpochDay(millisSinceLastBoot.milliseconds.inWholeDays)

        val momentEventTookPlace = millisSinceLastBoot.milliseconds.inWholeNanoseconds + timestampSinceBootNanos
        //println("--- STEP TRACKER COUNTER IMPL --- momentEventTookPlace: ${momentEventTookPlace}")
        val dateOfEvent = LocalDate.ofEpochDay(momentEventTookPlace.nanoseconds.inWholeDays)


        val days = (dateLastBooted.toEpochDay() .. dateOfEvent.toEpochDay()).step(1)

        for(day in days){
            val date = LocalDate.ofEpochDay(day)
            val data = SensorStepCounterData(
                yearLastBoot = date.year,
                monthLastBoot = date.monthValue,
                dayLastBoot = date.dayOfMonth,
                timeStampSinceBoot = timestampSinceBootNanos,
                numberSteps = steps
            )
            dao.saveSensorStepCounterData(data)
        }

    }

}