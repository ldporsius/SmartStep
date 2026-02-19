package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountUserOverride
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount

fun DailyStepCount.toUserOverrideEntity(): DailyStepCountUserOverride{

    return DailyStepCountUserOverride(
        dateEpochDay = dayEpochSeconds,
        stepCount = stepCount,
        userId = "todo"
    )
}

fun DailyStepCountUserOverride.toDomain(): DailyStepCount{
    return DailyStepCount(
        dayEpochSeconds = dateEpochDay,
        stepCount = stepCount
    )
}