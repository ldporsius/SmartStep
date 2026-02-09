package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model

import androidx.room.Entity

@Entity(tableName = "daily_step_count", primaryKeys = ["date", "userId"])
data class DailyStepCountEntity(
    val date: Long,
    val stepCount: Int,
    val userId: String
)

