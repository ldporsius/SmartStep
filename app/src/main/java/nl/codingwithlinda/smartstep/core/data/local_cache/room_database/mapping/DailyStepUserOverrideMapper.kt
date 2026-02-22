package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountUserOverride
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import java.time.LocalDate

fun DailyStepCount.toUserOverrideEntity(): DailyStepCountUserOverride{
    return DailyStepCountUserOverride(
        dateEpochDay = dayEpochDay,
        stepCount = stepCount,
        userId = "todo"
    )
}

fun DailyStepCountUserOverride.toDomain(): DailyStepCount{
    val step = DailyStepCountCreator.create(count = stepCount, date = this.dateEpochDay)
    return step
}