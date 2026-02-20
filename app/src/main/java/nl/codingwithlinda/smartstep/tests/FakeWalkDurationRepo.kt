package nl.codingwithlinda.smartstep.tests

import androidx.compose.ui.util.fastMaxOfOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationEnd
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD

class FakeWalkDurationRepo: WalkDurationRepo {

   val mutex = Mutex()
    private val _sessions = mutableListOf<WalkSession>()


    override suspend fun saveWalkDurationStart(walkDuration: WalkDurationStart) {

        mutex.withLock {
            val id = _sessions.fastMaxOfOrNull { it.id }?.plus(1) ?: 0
            _sessions.add(
                WalkSession(
                    id = id,
                    start = walkDuration,
                    end = null
                )
            )
        }
    }

    override suspend fun saveWalkDurationEnd(walkDuration: WalkDurationEnd) {
        mutex.withLock {
            val sessionToday = _sessions.filter {
                it.start.dateString == walkDuration.dateString
            }.filter {
                it.end == null
            }.minBy {
                it.start.timestamp
            }

            sessionToday.copy(
                end = walkDuration
            ).also {
                _sessions.remove(sessionToday)
                _sessions.add(it)
            }
        }
    }


    suspend fun saveStart(dateYYYYMMDD: DateYYYYMMDD, timestamp: Long){
        val start = WalkDurationStart(dateYYYYMMDD.YYYY, dateYYYYMMDD.MM, dateYYYYMMDD.DD, timestamp)
        saveWalkDurationStart(start)

    }
    suspend fun saveEnd(dateYYYYMMDD: DateYYYYMMDD, timestamp: Long){
        val end = WalkDurationEnd(dateYYYYMMDD.YYYY, dateYYYYMMDD.MM, dateYYYYMMDD.DD, timestamp)
        saveWalkDurationEnd(end)

    }

    override val sessions: Flow<List<WalkSession>>
        get() = flow {
           emit(_sessions)
        }
}