package nl.codingwithlinda.smartstep.core.data.local_cache.room_database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountBaseline
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountUserOverride

@Dao
interface DailyStepCountDao {

    @Upsert
    suspend fun saveDailyStepCount(dailyStepCount: DailyStepCountEntity)

    @Query("SELECT * FROM daily_step_count")
    fun getDailyStepCount(): Flow<List<DailyStepCountEntity>>

    @Upsert
    suspend fun saveDailyStepCountBaseline(dailyStepCountBaseline: DailyStepCountBaseline)

    @Query("SELECT * FROM daily_step_goal_baseline WHERE date = :date")
    suspend fun getDailyStepCountBaselineForDate(date: Long): DailyStepCountBaseline?


    @Upsert
    suspend fun saveDailyStepCountUserOverride(dailyStepCountUserOverride: DailyStepCountUserOverride)

    @Query("SELECT * FROM daily_step_count_user_override WHERE dateEpochDay = :dateEpochDay")
    suspend fun getDailyStepGoalUserOverrideForDay(dateEpochDay: Long): DailyStepCountUserOverride?

    @Query("SELECT * FROM daily_step_count_user_override")
    fun getDailyStepCountUserOverride(): Flow<List<DailyStepCountUserOverride>>


}