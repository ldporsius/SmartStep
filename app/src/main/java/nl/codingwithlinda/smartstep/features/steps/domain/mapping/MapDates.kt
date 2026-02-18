package nl.codingwithlinda.smartstep.features.steps.domain.mapping

import nl.codingwithlinda.smartstep.features.steps.domain.model.DateYYYYMMDD
import java.time.LocalDate
import kotlin.time.Duration.Companion.days

fun DateYYYYMMDD.toDomain(): Long{
    val localDate = LocalDate.of(YYYY, MM, DD)
    return localDate.toEpochDay().days.inWholeSeconds
}
