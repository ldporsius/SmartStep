package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.milliseconds

data class DailyStepCount(
    val YYYY: Int,
    val MM: Int,
    val DD: Int,
    val stepCount: Int
){
    fun dayEpochSeconds(): Long = LocalDate.of(YYYY, MM, DD).toEpochDay()

    fun parseMillisToLocalDate(
        millis: Long,
        zoneOffset: ZoneOffset
    ): LocalDate {
        val isValidMillis = millis.milliseconds.inWholeDays in -50_000 .. 50_000
        if(!isValidMillis) return LocalDate.ofEpochDay(0)

        return LocalDateTime.ofEpochSecond(millis,0,zoneOffset).toLocalDate()
    }

}