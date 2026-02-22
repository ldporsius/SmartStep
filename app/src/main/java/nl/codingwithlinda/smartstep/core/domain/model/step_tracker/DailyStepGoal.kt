package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import java.time.LocalDate

data class DailyStepGoal(
    val YYYY: Int,
    val MM: Int,
    val DD: Int,
    val goal: Int
){
    val epochDay: Long
        get() = LocalDate.of(YYYY, MM, DD).toEpochDay()
}
