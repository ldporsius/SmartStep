package nl.codingwithlinda.smartstep.core.domain.util.factories

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import java.time.LocalDate
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object DateTimeHelper {

    enum class EpochValue{
        MILLIS,
        SECONDS,
        DAYS
    }
    fun localDateFromMillis(date: Long): LocalDate{
        val epochValue = validateLongInput(date)
        println("${date} with lengt: ${date.toString().length} is epoch value: $epochValue")

        val dateInDays = when(epochValue){
            EpochValue.MILLIS -> date.milliseconds.inWholeDays
            EpochValue.SECONDS -> date.seconds.inWholeDays
            EpochValue.DAYS -> date.days.inWholeDays
        }

        println("dateInDays: ${dateInDays.days}")
        val localDate = LocalDate.ofEpochDay(dateInDays)

        println("localDate: $localDate")
        return localDate

    }


    fun toDateYYYYMMDD(
        dayEpochSeconds: Long,
        ): DateYYYYMMDD {
        val isInputMillis = dayEpochSeconds.toString().length >= 13
        val day = when(isInputMillis) {
            true -> dayEpochSeconds.milliseconds.inWholeDays
            false -> dayEpochSeconds.seconds.inWholeDays
        }
        val local = LocalDate.ofEpochDay(day)

        return DateYYYYMMDD(
            YYYY = local.year,
            MM = local.monthValue,
            DD = local.dayOfMonth
        )
    }

    private fun validateLongInput(input: Long): EpochValue{
        val isDateMillis = input.toString().length >= 13
        if (isDateMillis) return EpochValue.MILLIS

        if (input.toString().length >= 9)
        return EpochValue.SECONDS

        return EpochValue.DAYS

    }
}