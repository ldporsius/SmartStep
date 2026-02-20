package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import java.time.LocalDate

data class DateYYYYMMDD(
    val YYYY: Int,
    val MM: Int,
    val DD: Int
){
    val dateString: String
        get() = "$YYYY/$MM/$DD"
    val dateEpochDay: Long
        get() = LocalDate.of(YYYY, MM, DD).toEpochDay()

}