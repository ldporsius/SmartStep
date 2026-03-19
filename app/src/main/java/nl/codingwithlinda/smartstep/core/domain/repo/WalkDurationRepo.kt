package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession

interface WalkDurationRepo {
    suspend fun saveWalkDurationStart(timestampMillis: Long)

    suspend fun saveWalkDurationEnd(timestampMillis: Long)

    val sessions : Flow<List<WalkSession>>

}