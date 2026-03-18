package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.WalkSessionEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationEnd
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession

fun WalkSessionEntity.toDomain(): WalkSession {

    val endDuration = this.endTimestampMillis.let {
        if (it == null) null
        else WalkDurationEnd(
            timestamp = it
        )
    }
    return WalkSession(
        id = this.startTimestampMillis,
        start = WalkDurationStart(
            timestamp = this.startTimestampMillis
        ),
        end = endDuration
    )
}