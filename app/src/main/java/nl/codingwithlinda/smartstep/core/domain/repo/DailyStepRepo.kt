package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal

interface DailyStepRepo {
    suspend fun saveDailyStepGoal(dailyStepGoal: DailyStepGoal)
    fun getDailyStepGoals(): Flow<List<DailyStepGoal>>
    suspend fun getDailyStepGoalsForUser(): List<DailyStepGoal>

    suspend fun saveStepCount(stepCount: DailyStepCount)
    val stepCount: Flow<DailyStepCount>
}

