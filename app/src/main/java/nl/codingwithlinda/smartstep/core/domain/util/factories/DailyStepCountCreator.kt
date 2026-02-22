package nl.codingwithlinda.smartstep.core.domain.util.factories

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import java.time.LocalDate
import java.time.temporal.TemporalQueries.localDate
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object DailyStepCountCreator {

    fun create(count: Int, date: DateYYYYMMDD): DailyStepCount {
        return DailyStepCount(
            YYYY = date.YYYY,
            MM = date.MM,
            DD = date.DD,
            stepCount = count
        )
    }

    fun create(count: Int, date: Long = System.currentTimeMillis()): DailyStepCount {
       val localDate = DateTimeHelper.localDateFromMillis(date)
        return DailyStepCount(
            YYYY = localDate.year,
            MM = localDate.monthValue,
            DD = localDate.dayOfMonth,
            stepCount = count.coerceAtLeast(0)
        )
    }

}