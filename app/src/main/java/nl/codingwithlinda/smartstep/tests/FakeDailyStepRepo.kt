package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import java.time.LocalDate

class FakeDailyStepRepo: DailyStepRepo {

    val dateToday = LocalDate.of(2026, 2, 21)
    private val goals =
        listOf(DailyStepGoal(
            YYYY = dateToday.year,
            MM = dateToday.monthValue,
            DD = dateToday.dayOfMonth,
            1000))

    private val goalObservable = MutableStateFlow<DailyStepGoal?>(null)
    private val _stepCount:MutableStateFlow<List<DailyStepCount>> = MutableStateFlow(emptyList())

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

    override suspend fun getDailyStepGoalsLatest(): List<DailyStepGoal> {
        return goals
    }

    override suspend fun saveStepCount(stepCount: DailyStepCount) {
        println("--- FakeDailyStepRepo --- saveStepCount: $stepCount")
        val current = getStepCountForDate(stepCount.dayEpochDay)
        current?.run {
            _stepCount.update {
                it.minus(this)
            }
        }
        _stepCount.update {
            it.plus(stepCount)
        }
    }

    override suspend fun getStepCountForDate(date: Long): DailyStepCount? {
        return _stepCount.value.firstOrNull(){ it.dayEpochDay == date }
    }


    suspend fun addStepCountToToday(stepCount: DailyStepCount) {
        val current = getStepCountForDate(stepCount.dayEpochDay)
        current?.run {
            _stepCount.update {
                it.minus(this)
            }
        }
        val update = current?.let {
            it.copy(stepCount = it.stepCount + stepCount.stepCount)
        }
            ?: stepCount
        _stepCount.update {
            it.plus(update)
        }
    }

    override val stepCount: Flow<List<DailyStepCount>> = _stepCount

    override suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount) {
        _baseline.update {
            dailyStepCount
        }
    }

    override suspend fun getDailyStepCountBaselineForDate(date: DateYYYYMMDD): DailyStepCount? {
        return _baseline.value
    }

    override suspend fun saveDailyStepCountUserOverride(dailyStepCount: DailyStepCount) {
        val current = getDailyStepCountUserOverrideForDay(dailyStepCount.dayEpochDay) ?: dailyStepCount
        _userStepCountOverride.update {
            it.minus(current).plus(dailyStepCount)
        }
    }

    override suspend fun getDailyStepCountUserOverrideForDay(date: Long): DailyStepCount? {
        return _userStepCountOverride.value.singleOrNull {
            it.dayEpochDay == date
        }
    }

    override fun getDailyStepCountUserOverride(): Flow<List<DailyStepCount>> = _userStepCountOverride
    override val stepCountPlusUserOverride: Flow<List<DailyStepCount>>
        get() = combine(stepCount, _userStepCountOverride){
                steps, override ->
            steps.associateWith { step ->
                override.find {
                    it.dateYYYYMMDD == step.dateYYYYMMDD
                }
            }.map { (step, override) ->
                DailyStepCountCreator.create(
                    step.stepCount + (override?.stepCount ?: 0),
                    step.dateYYYYMMDD
                )
            }
        }


    fun reset() {
        _baseline.update {
            null
        }
        _stepCount.update {
            emptyList()
        }
        _userStepCountOverride.update {
            emptyList()
        }
    }
}