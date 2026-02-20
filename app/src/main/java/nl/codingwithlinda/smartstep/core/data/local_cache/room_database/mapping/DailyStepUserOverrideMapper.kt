package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountUserOverride
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import java.time.LocalDate

fun DailyStepCount.toUserOverrideEntity(): DailyStepCountUserOverride{

    val dayEpochDay = this.dayEpochSeconds()
    return DailyStepCountUserOverride(
        dateEpochDay = dayEpochDay,
        stepCount = stepCount,
        userId = "todo"
    )
}

fun DailyStepCountUserOverride.toDomain(): DailyStepCount{
    val dateEpochDay = LocalDate.ofEpochDay(dateEpochDay)
    return DailyStepCount(
        YYYY = dateEpochDay.year,
        MM = dateEpochDay.monthValue,
        DD = dateEpochDay.dayOfMonth,
        stepCount = stepCount
    )
}