package nl.codingwithlinda.smartstep

import androidx.compose.ui.util.fastMaxOfOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDuration

class FakeWalkDurationRepo: WalkDurationRepo {

   val mutex = Mutex()
    private val _sessions = mutableListOf<WalkSession>()


    override suspend fun saveWalkDurationStart(timestampMillis: Long) {

        mutex.withLock {
            val id = _sessions.fastMaxOfOrNull { it.id }?.plus(1) ?: 0
            _sessions.add(
                WalkSession(
                    id = id,
                    start = WalkDuration(timestampMillis),
                    end = null
                )
            )
        }
    }

    override suspend fun saveWalkDurationEnd(timestampMillis: Long) {
        mutex.withLock {
            val sessionToday = _sessions.filter {
                it.start.dateString == WalkDuration(timestampMillis).dateString
            }.filter {
                it.end == null
            }.minBy {
                it.start.timestamp
            }

            sessionToday.copy(
                end = WalkDuration(timestampMillis)
            ).also {
                _sessions.remove(sessionToday)
                _sessions.add(it)
            }
        }
    }


    suspend fun saveStart(dateYYYYMMDD: DateYYYYMMDD, timestamp: Long){
        saveWalkDurationStart(timestamp)

    }
    suspend fun saveEnd(dateYYYYMMDD: DateYYYYMMDD, timestamp: Long){
        saveWalkDurationEnd(timestamp)

    }

    override val sessions: Flow<List<WalkSession>>
        get() = flow {
           emit(_sessions)
        }
}