package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.features.steps.domain.model.DateYYYYMMDD
import java.time.LocalDate

class FakeDailyStepRepo: DailyStepRepo {

    private val goals =
        listOf(DailyStepGoal(1, 1000))

    private val goalObservable = MutableStateFlow<DailyStepGoal?>(null)
    private val _stepCount = MutableStateFlow(DailyStepCount(0, 0))

    private val _baseline = MutableStateFlow<DailyStepCount?>(null)

    override suspend fun saveDailyStepGoal(dailyStepGoal: DailyStepGoal) {
        goalObservable.update {
            dailyStepGoal
        }
    }

    override fun getDailyStepGoals(): Flow<List<DailyStepGoal>> = flow {
        emit(goals)
    }

    override suspend fun getDailyStepGoalsForUser(): List<DailyStepGoal> {
        return goals
    }

    override suspend fun saveStepCount(stepCount: DailyStepCount) {
        _stepCount.update {
            stepCount
        }
    }

    override suspend fun getStepCountForDate(date: Long): DailyStepCount? {
        return _stepCount.value.takeIf { it.date == date }
    }

    fun getStepCountForYYYYMMDD(dateYYYYMMDD: DateYYYYMMDD): DailyStepCount?{
        val date = DailyStepCountCreator.fromDateYYYYMMDD(dateYYYYMMDD)
        return _stepCount.value.takeIf { it.date == date }
    }

    override suspend fun addStepCountToToday(stepCount: DailyStepCount) {
        _stepCount.update {
            it.copy(stepCount = it.stepCount + stepCount.stepCount)
        }
    }

    override val stepCount: Flow<DailyStepCount> = _stepCount

    override suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount) {
        _baseline.update {
            dailyStepCount
        }
    }

    override suspend fun getDailyStepCountBaselineForDate(date: Long): DailyStepCount? {
        return _baseline.value
    }

}