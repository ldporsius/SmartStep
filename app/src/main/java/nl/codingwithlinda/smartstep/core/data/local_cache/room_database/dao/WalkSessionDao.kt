package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.WalkSessionEntity


@Dao
interface WalkSessionDao {

    @Upsert
    suspend fun insertWalkSession(walkSession: WalkSessionEntity)

    @Query("SELECT * FROM WalkSessionEntity WHERE startTimestampMillis = :startTimestampMillis")
    suspend fun getWalkSessionByStartTimestamp(startTimestampMillis: Long): WalkSessionEntity?

    @Query("SELECT * FROM WalkSessionEntity")
    fun getAllWalkSessionsAsFlow(): Flow<List<WalkSessionEntity>>

    @Query("SELECT * FROM WalkSessionEntity")
    suspend fun getAllWalkSessions(): List<WalkSessionEntity>


}