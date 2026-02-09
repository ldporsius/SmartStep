package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model

import androidx.room.Entity

@Entity(
    tableName = "daily_step_goal",
    primaryKeys = ["date", "userId"]
)
data class DailyStepGoalEntity(
    val date: Long,
    val goal: Int,
    val userId: String
)
