package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountBaseline
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import java.time.LocalDate

fun DailyStepCount.toBaselineEntity(): DailyStepCountBaseline{

    return DailyStepCountBaseline(
        date = dayEpochDay,
        steps = stepCount
    )
}

fun DailyStepCountBaseline.toDomain(): DailyStepCount{
    val local = DailyStepCountCreator.toDateYYYYMMDD(this.date)
    return DailyStepCount(
        YYYY = local.YYYY,
        MM = local.MM,
        DD = local.DD,
        stepCount = steps
    )
}