package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao

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

}