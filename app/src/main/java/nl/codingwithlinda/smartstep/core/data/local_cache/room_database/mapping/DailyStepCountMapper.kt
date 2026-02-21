package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCountCreator
import java.time.LocalDate

fun DailyStepCountEntity.toDomain(): DailyStepCount{
    return DailyStepCountCreator.create(
        count = stepCount,
        date = this.date
    )
}

fun DailyStepCount.toEntity(userId: String): DailyStepCountEntity {
    return DailyStepCountEntity(
        date = dayEpochDay,
        stepCount = stepCount,
        userId = userId
    )
}