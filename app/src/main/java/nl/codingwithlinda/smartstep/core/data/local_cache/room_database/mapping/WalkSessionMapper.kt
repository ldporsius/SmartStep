package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.WalkSessionEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDuration
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession

fun WalkSessionEntity.toDomain(): WalkSession {

    val endDuration = this.endTimestampMillis.let {
        if (it == null) null
        else WalkDuration(
            timestamp = it
        )
    }
    return WalkSession(
        id = this.startTimestampMillis,
        start = WalkDuration(
            timestamp = this.startTimestampMillis
        ),
        end = endDuration
    )
}