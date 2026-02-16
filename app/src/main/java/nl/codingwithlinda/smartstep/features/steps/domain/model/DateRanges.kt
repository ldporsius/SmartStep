package nl.codingwithlinda.smartstep.features.steps.domain.model

import java.time.YearMonth


val years = IntRange(2000, 2025)
val months = IntRange(1, 12)


data class DatePicker(
    val year: Int
){
    val months = nl.codingwithlinda.smartstep.features.steps.domain.model.months
    val daysInMonth = months.map { month ->
        YearMonth.of(year, month).lengthOfMonth()
    }
}
val datePicker = years.map {
    DatePicker(it)
}