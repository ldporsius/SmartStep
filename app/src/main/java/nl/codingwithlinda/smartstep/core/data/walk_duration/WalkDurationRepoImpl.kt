package nl.codingwithlinda.smartstep.core.data.walk_duration

import androidx.compose.ui.util.fastMaxOfOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationEnd
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo

class WalkDurationRepoImpl: WalkDurationRepo {

    val mutex = Mutex()
    private val _sessions = MutableStateFlow< List<WalkSession>>(emptyList())


    override suspend fun saveWalkDurationStart(walkDuration: WalkDurationStart) {

        mutex.withLock {
            val id = _sessions.value.fastMaxOfOrNull { it.id }?.plus(1) ?: 0
            val update =
                WalkSession(
                    id = id,
                    start = walkDuration,
                    end = null
                )
            _sessions.update {
                it.plus(update)
            }
        }
    }

    override suspend fun saveWalkDurationEnd(walkDuration: WalkDurationEnd) {

        mutex.withLock {
            val sessionToday = _sessions.value.filter {
                it.start.dateString == walkDuration.dateString
            }.filter {
                it.end == null
            }.minByOrNull {
                it.start.timestamp
            }

            println("--- WalkDurationRepoImpl --- sessionToday: $sessionToday")


            sessionToday?.copy(
                end = walkDuration
            )?.also {update ->
                _sessions.update {
                    it.minus(sessionToday).plus(update)
                }
            }

            println("--- WalkDurationRepoImpl --- _sessions: ${_sessions.value}")
        }
    }

    override val sessions: Flow<List<WalkSession>>
         = _sessions
}