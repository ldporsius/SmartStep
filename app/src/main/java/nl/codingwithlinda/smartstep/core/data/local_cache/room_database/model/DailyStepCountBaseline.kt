package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model

import androidx.room.Entity

@Entity(
    tableName = "daily_step_goal_baseline",
    primaryKeys = ["date"]
)
data class DailyStepCountBaseline(
    val date: Long,
    val steps: Int
)
