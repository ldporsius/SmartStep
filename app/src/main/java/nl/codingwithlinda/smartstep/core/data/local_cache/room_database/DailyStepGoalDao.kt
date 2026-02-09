package nl.codingwithlinda.smartstep.core.data.local_cache.room_database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepGoalEntity

@Dao
interface DailyStepGoalDao {

    @Query("SELECT * FROM daily_step_goal WHERE date = :date AND userId = :userId")
    suspend fun getDailyStepGoal(date: Long, userId: String): DailyStepGoalEntity?

    @Query("SELECT * FROM daily_step_goal")
    fun getAllDailyStepGoals(): Flow<List<DailyStepGoalEntity>>

    @Upsert
    suspend fun upsertDailyStepGoal(dailyStepGoalEntity: DailyStepGoalEntity)



}