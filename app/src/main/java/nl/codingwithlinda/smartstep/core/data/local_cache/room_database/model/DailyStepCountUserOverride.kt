package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model

import androidx.room.Entity

@Entity(
    primaryKeys = ["dateEpochDay", "userId"],
    tableName = "daily_step_count_user_override"
)
data class DailyStepCountUserOverride(
    val dateEpochDay: Long,
    val stepCount: Int,
    val userId: String
)
