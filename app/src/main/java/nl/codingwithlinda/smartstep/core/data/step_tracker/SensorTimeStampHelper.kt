package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.os.SystemClock
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

object SensorTimeStampHelper {

    fun timeStampToLocalDate(timeInNanos: Long): LocalDate{
        val deviceLastBootedDate = System.currentTimeMillis() - SystemClock.elapsedRealtime()

        val momentEventTookPlace = deviceLastBootedDate.milliseconds.inWholeNanoseconds + timeInNanos
        val dateOfEvent = LocalDate.ofEpochDay(momentEventTookPlace.nanoseconds.inWholeDays)

        return dateOfEvent

    }

    fun localDateToDomain(localDate: LocalDate): DateYYYYMMDD{
        return  DateYYYYMMDD(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }
}