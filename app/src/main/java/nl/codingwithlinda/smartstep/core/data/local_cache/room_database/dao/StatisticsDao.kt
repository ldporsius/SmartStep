package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.StatisticsEntity


@Dao
interface StatisticsDao {

    @Upsert
    suspend fun saveStatistics(statistics: StatisticsEntity)

    @Query("SELECT * FROM StatisticsEntity WHERE dayEpoch = :dayEpoch")
    suspend fun getStatisticsForDay(dayEpoch: Long): StatisticsEntity?

    @Query("SELECT * FROM StatisticsEntity")
    suspend fun getAllStatistics(): List<StatisticsEntity>


}