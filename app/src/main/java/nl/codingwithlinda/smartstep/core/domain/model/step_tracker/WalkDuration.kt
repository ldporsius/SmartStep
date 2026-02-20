package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

data class WalkDurationStart(
    val YYYY: Int,
    val MM: Int,
    val DD: Int,
    val timestamp: Long,
){
    val dateString: String
        get() = dateYYYYMMDD.dateString
    val dateYYYYMMDD : DateYYYYMMDD
        get() = DateYYYYMMDD(YYYY, MM, DD)
}

data class WalkDurationEnd(
    val YYYY: Int,
    val MM: Int,
    val DD: Int,
    val timestamp: Long,
){
    val dateString: String
        get() = DateYYYYMMDD(YYYY, MM, DD).dateString
}

data class WalkSession(
    val id: Long,
    val start: WalkDurationStart,
    val end: WalkDurationEnd? = null,
)
