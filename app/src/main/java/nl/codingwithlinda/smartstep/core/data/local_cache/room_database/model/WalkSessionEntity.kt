package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WalkSessionEntity(
    @PrimaryKey
    val startTimestampMillis: Long,
    val endTimestampMillis: Long? = null,
    )
