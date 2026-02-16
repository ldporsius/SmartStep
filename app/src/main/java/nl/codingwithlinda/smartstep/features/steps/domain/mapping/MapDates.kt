package nl.codingwithlinda.smartstep.features.steps.domain.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.features.steps.domain.model.DateYYYYMMDD
import java.time.LocalDate

fun DateYYYYMMDD.toDomain(): Long{
    val localDate = LocalDate.of(YYYY, MM, DD)
    return localDate.toEpochDay()
}

fun DailyStepCount.toDateYYYYMMDD(): DateYYYYMMDD{
    return DailyStepCountCreator.toDateYYYYMMDD(this.date)
}