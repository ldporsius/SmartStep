package nl.codingwithlinda.smartstep

import android.R.attr.end
import android.provider.SyncStateContract.Helpers.update
import androidx.compose.ui.util.fastMaxOfOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDuration

class FakeWalkDurationRepo: WalkDurationRepo {

    val mutex = Mutex()
    private val _sessions = mutableMapOf<Long, WalkSession>()


    override suspend fun saveWalkDurationStart(timestampMillis: Long) {

        mutex.withLock(this) {
            _sessions[timestampMillis] =
                WalkSession(
                    id = timestampMillis,
                    start = WalkDuration(timestampMillis),
                    end = null
                )
        }
    }

    override suspend fun saveWalkDurationEnd(timestampMillis: Long) {
        mutex.withLock(this) {
            val latestUnfinishedSession = _sessions.filter {
                it.value.start.dateYYYYMMDD.dateEpochDay == WalkDuration(timestampMillis).dateYYYYMMDD.dateEpochDay
            }.filter {
                it.value.end == null
            }.minByOrNull {
                it.key
            }?.value

            val update = latestUnfinishedSession?.copy(
                end = WalkDuration(timestampMillis)
            )?: return

            _sessions.put(update.id, update)

        }
    }


    override val sessions: Flow<List<WalkSession>>
        get() = flow {
            emit(_sessions.map {
                it.value
            })
        }
}