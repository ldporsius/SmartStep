package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper

data class WalkDurationStart(
    val timestamp: Long,
){
    private val toLocalDate = DateTimeHelper.toDateYYYYMMDD(timestamp)
    val dateString: String
        get() = dateYYYYMMDD.dateString
    val dateYYYYMMDD : DateYYYYMMDD
        get() = toLocalDate
}

data class WalkDurationEnd(
    val timestamp: Long,
){
    private val toLocalDate = DateTimeHelper.toDateYYYYMMDD(timestamp)
    val dateString: String
        get() = toLocalDate.dateString
}

data class WalkSession(
    val id: Long,
    val start: WalkDurationStart,
    val end: WalkDurationEnd? = null,
)
