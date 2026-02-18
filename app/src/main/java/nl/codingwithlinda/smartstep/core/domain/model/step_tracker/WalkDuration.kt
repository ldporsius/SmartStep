package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import nl.codingwithlinda.smartstep.features.steps.domain.mapping.toDomain
import nl.codingwithlinda.smartstep.features.steps.domain.model.DateYYYYMMDD

data class WalkDurationStart(
    val YYYY: Int,
    val MM: Int,
    val DD: Int,
    val timestamp: Long,
){
    val dateSeconds: Long
        get() = DateYYYYMMDD(YYYY, MM, DD).toDomain()
}

data class WalkDurationEnd(
    val YYYY: Int,
    val MM: Int,
    val DD: Int,
    val timestamp: Long,
){
    val dateSeconds: Long
        get() = DateYYYYMMDD(YYYY, MM, DD).toDomain()
}

data class WalkSession(
    val id: Long,
    val start: WalkDurationStart,
    val end: WalkDurationEnd? = null,
)
