package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import java.time.LocalDate

fun DailyStepCountEntity.toDomain(): DailyStepCount{
    val  dayEpochSeconds = LocalDate.ofEpochDay(this.date)
    return DailyStepCount(
        YYYY = dayEpochSeconds.year,
        MM = dayEpochSeconds.monthValue,
        DD = dayEpochSeconds.dayOfMonth,
        stepCount = stepCount
    )
}

fun DailyStepCount.toEntity(userId: String): DailyStepCountEntity {
    val  dayEpochSeconds = this.dayEpochSeconds()
    return DailyStepCountEntity(
        date = dayEpochSeconds,
        stepCount = stepCount,
        userId = userId
    )
}