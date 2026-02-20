package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountBaseline
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import java.time.LocalDate

fun DailyStepCount.toBaselineEntity(): DailyStepCountBaseline{
    val dayEpochSeconds = this.dayEpochSeconds()
    return DailyStepCountBaseline(
        date = dayEpochSeconds,
        steps = stepCount
    )
}

fun DailyStepCountBaseline.toDomain(): DailyStepCount{
    val local = LocalDate.ofEpochDay(date)
    return DailyStepCount(
        YYYY = local.year,
        MM = local.monthValue,
        DD = local.dayOfMonth,
        stepCount = steps
    )
}