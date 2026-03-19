package nl.codingwithlinda.smartstep.core.data.repo.walk_duration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.WalkSessionDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toDomain
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.WalkSessionEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo

class WalkDurationRepoImpl(
    private val dao: WalkSessionDao
): WalkDurationRepo {

    private val _sessions = dao.getAllWalkSessionsAsFlow().map {list ->
        list.sortedByDescending { it.startTimestampMillis }
            .mapIndexed { index, entity ->
            WalkSessionEntity(
                startTimestampMillis = entity.startTimestampMillis,
                endTimestampMillis = list.getOrNull(index+1)?.startTimestampMillis
            )
        }
    }

    override suspend fun saveWalkDurationStart(timestampMillis: Long) {
        val entity = WalkSessionEntity(
            startTimestampMillis = timestampMillis,
            endTimestampMillis = null
        )
        dao.insertWalkSession(entity)
    }

    override suspend fun saveWalkDurationEnd(timestampMillis: Long) {

            val sessionToday = dao.getAllWalkSessions().map {
                    it.toDomain()
            }.maxByOrNull {
                it.start.timestamp}

            println("--- WalkDurationRepoImpl --- sessionToday: $sessionToday")

            sessionToday?.let {
            update ->
                val entity = WalkSessionEntity(
                    startTimestampMillis = update.start.timestamp,
                    endTimestampMillis = timestampMillis
                )
                dao.insertWalkSession(entity)
            }
    }

    override val sessions: Flow<List<WalkSession>>
         = _sessions.map { entities ->
        entities.map {
                 it.toDomain()
             }
    }
}