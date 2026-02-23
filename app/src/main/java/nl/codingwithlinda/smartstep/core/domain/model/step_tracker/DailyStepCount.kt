package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import java.time.LocalDate

data class DailyStepCount(
    val YYYY: Int,
    val MM: Int,
    val DD: Int,
    val stepCount: Int
){
    val dayEpochDay: Long
        get() = LocalDate.of(YYYY, MM, DD).toEpochDay()

    val dateYYYYMMDD : DateYYYYMMDD
        = DateYYYYMMDD(YYYY, MM, DD)

}
