package nl.codingwithlinda.smartstep.core.data.local_cache.room_database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountEntity

@Dao
interface DailyStepCountDao {

    @Upsert
    suspend fun saveDailyStepCount(dailyStepCount: DailyStepCountEntity)

    @Query("SELECT * FROM daily_step_count")
    fun getDailyStepCount(): Flow<List<DailyStepCountEntity>>

}