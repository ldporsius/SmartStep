package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount

fun DailyStepCountEntity.toDomain(): DailyStepCount{
    return DailyStepCount(
        dateSeconds = date,
        stepCount = stepCount
    )
}

fun DailyStepCount.toEntity(userId: String): DailyStepCountEntity {
    return DailyStepCountEntity(
        date = dateSeconds,
        stepCount = stepCount,
        userId = userId
    )
}