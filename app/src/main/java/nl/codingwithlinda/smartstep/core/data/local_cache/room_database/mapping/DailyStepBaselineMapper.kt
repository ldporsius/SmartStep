package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountBaseline
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount

fun DailyStepCount.toBaselineEntity(): DailyStepCountBaseline{
    return DailyStepCountBaseline(
        date = dateSeconds,
        steps = stepCount
    )
}

fun DailyStepCountBaseline.toDomain(): DailyStepCount{
    return DailyStepCount(
        dateSeconds = date,
        stepCount = steps
    )
}