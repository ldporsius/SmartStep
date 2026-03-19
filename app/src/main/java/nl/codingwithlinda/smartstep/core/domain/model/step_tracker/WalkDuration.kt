package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper

data class WalkDuration(
    val timestamp: Long,
){
    val dateString: String
        get() = dateYYYYMMDD.dateString
    val dateYYYYMMDD : DateYYYYMMDD
        get() = DateTimeHelper.toDateYYYYMMDD(timestamp)
}


data class WalkSession(
    val id: Long,
    val start: WalkDuration,
    val end: WalkDuration? = null,
)
