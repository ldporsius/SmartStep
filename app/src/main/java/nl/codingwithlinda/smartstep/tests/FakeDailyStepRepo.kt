package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD

class FakeDailyStepRepo: DailyStepRepo {

    val today = DailyStepCountCreator.getTodayAsSeconds()
    private val goals =
        listOf(DailyStepGoal(1, 1000))

    private val goalObservable = MutableStateFlow<DailyStepGoal?>(null)
    private val _stepCount:MutableStateFlow<DailyStepCount?> = MutableStateFlow(null)

    private val _userStepCountOverride = MutableStateFlow<List<DailyStepCount>>(emptyList())

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
        return _stepCount.value.takeIf { it?.dayEpochSeconds() == date }
    }

    fun getStepCountForYYYYMMDD(dateYYYYMMDD: DateYYYYMMDD): DailyStepCount?{
       throw Exception("not implemented")
    }

    override suspend fun addStepCountToToday(stepCount: DailyStepCount) {
        val current = _stepCount.value?.let {
            it.copy(stepCount = it.stepCount + stepCount.stepCount)
            }
            ?: stepCount
        this@FakeDailyStepRepo._stepCount.update {
           current
        }
    }

    override val stepCount: Flow<DailyStepCount> = _stepCount.mapNotNull {it}

    override suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount) {
        _baseline.update {
            dailyStepCount
        }
    }

    override suspend fun getDailyStepCountBaselineForDate(date: Long): DailyStepCount? {
        return _baseline.value
    }

    override suspend fun saveDailyStepCountUserOverride(dailyStepCount: DailyStepCount) {
        _userStepCountOverride.update {
            it + dailyStepCount
        }
    }

    override suspend fun getDailyStepCountUserOverrideForDay(date: Long): DailyStepCount? {
        return _userStepCountOverride.value.firstOrNull {
            it.dayEpochSeconds() == date
        }
    }

    override fun getDailyStepCountUserOverride(): Flow<List<DailyStepCount>> = _userStepCountOverride


    fun reset() {
        _baseline.update {
            null
        }
        _stepCount.update {
            null
        }
        _userStepCountOverride.update {
            emptyList()
        }
    }
}