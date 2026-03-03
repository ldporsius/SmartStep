package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD

interface DailyStepRepo {
    suspend fun saveDailyStepGoal(dailyStepGoal: DailyStepGoal)
    fun getDailyStepGoals(): Flow<List<DailyStepGoal>>
    suspend fun getDailyStepGoalsLatest(): List<DailyStepGoal>

    suspend fun getGoalForDay(dateYYYYMMDD: DateYYYYMMDD): DailyStepGoal?

    //////////////////////////////////////////////////////////////////////////

    suspend fun saveDailyStepCountUserOverride(dateYYYYMMDD: DateYYYYMMDD, stepCount: Int)

    val stepCountPlusUserOverride : Flow< List<DailyStepCount>>


}

