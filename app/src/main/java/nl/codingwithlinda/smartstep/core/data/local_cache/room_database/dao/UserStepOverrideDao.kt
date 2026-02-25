package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountUserOverride

@Dao
interface UserStepOverrideDao {

    @Upsert
    suspend fun saveDailyStepUserOverride(dailyStepCountUserOverride: DailyStepCountUserOverride)

    @Query("SELECT * FROM daily_step_count_user_override WHERE dateEpochDay = :dateEpochDay")
    suspend fun getDailyStepUserOverrideForDay(dateEpochDay: Long): DailyStepCountUserOverride?

    @Query("SELECT * FROM daily_step_count_user_override")
    fun getDailyStepCountUserOverride(): Flow<List<DailyStepCountUserOverride>>

}