package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationEnd
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession

interface WalkDurationRepo {
    suspend fun saveWalkDurationStart(walkDuration: WalkDurationStart)

    suspend fun saveWalkDurationEnd(walkDuration: WalkDurationEnd)

    val sessions : Flow<List<WalkSession>>

}