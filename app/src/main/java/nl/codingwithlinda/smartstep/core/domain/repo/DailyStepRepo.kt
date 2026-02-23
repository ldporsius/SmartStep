package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD

interface DailyStepRepo {
    suspend fun saveDailyStepGoal(dailyStepGoal: DailyStepGoal)
    fun getDailyStepGoals(): Flow<List<DailyStepGoal>>
    suspend fun getDailyStepGoalsLatest(): List<DailyStepGoal>

    //////////////////////////////////////////////////////////////////////////
    suspend fun saveStepCount(stepCount: DailyStepCount)
    suspend fun getStepCountForDate(date: Long): DailyStepCount?

    val stepCount: Flow<List<DailyStepCount>>

    /////////////////////////////////////////////////////////////////////
    suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount)
    suspend fun getDailyStepCountBaselineForDate(date: DateYYYYMMDD): DailyStepCount?

    suspend fun saveDailyStepCountUserOverride(dailyStepCount: DailyStepCount)

    suspend fun getDailyStepCountUserOverrideForDay(date: Long): DailyStepCount?

    fun getDailyStepCountUserOverride(): Flow<List<DailyStepCount>>

}

