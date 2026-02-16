package nl.codingwithlinda.smartstep.features.steps.domain.model

import java.time.YearMonth


val years = IntRange(2000, 2031)
val months = IntRange(1, 12)


data class DatePicker(
    val year: Int,
){
    val months = nl.codingwithlinda.smartstep.features.steps.domain.model.months
    val daysInMonths = months.associate { month ->
        val max = YearMonth.of(year, month).lengthOfMonth()
        month to IntRange(1, max)
    }

    fun daysInMonth(month: Int): IntRange{
        return daysInMonths.get(month) ?: IntRange.EMPTY
    }

}

