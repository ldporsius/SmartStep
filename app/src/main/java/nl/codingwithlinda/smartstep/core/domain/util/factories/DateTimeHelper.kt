package nl.codingwithlinda.smartstep.core.domain.util.factories

import android.R.id.input
import androidx.core.util.toRange
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import java.time.LocalDate
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object DateTimeHelper {

    enum class EpochValue{
        MILLIS,
        SECONDS,
        DAYS,
    }

    fun toDateYYYYMMDD(
        dayEpochSeconds: Long,
        ): DateYYYYMMDD {
        val epochValue = validateLongInput(dayEpochSeconds)
        println("${dayEpochSeconds} with lengt: ${dayEpochSeconds.toString().length} is epoch value: $epochValue")

        val day = when(epochValue) {
            EpochValue.MILLIS -> dayEpochSeconds.milliseconds.inWholeDays
            EpochValue.SECONDS -> dayEpochSeconds.seconds.inWholeDays
            EpochValue.DAYS -> dayEpochSeconds.days.inWholeDays

        }

        val local = if(!isInDayRange(day))
            LocalDate.ofEpochDay(LocalDate.MIN.toEpochDay())
            else
            LocalDate.ofEpochDay(day)

        return DateYYYYMMDD(
            YYYY = local.year,
            MM = local.monthValue,
            DD = local.dayOfMonth
        )
    }

    private fun isInDayRange(day: Long): Boolean {
        val lowest = LocalDate.MIN.toEpochDay()
        val highest = LocalDate.MAX.toEpochDay()
        val validDateRange = lowest..highest

        return (day in validDateRange)

    }

    private fun validateLongInput(input: Long): EpochValue{

       if(input.toString().length >= 13)
            return EpochValue.MILLIS

        if (input.toString().length >= 9)
            return EpochValue.SECONDS

        return EpochValue.DAYS

    }
}