package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import java.time.LocalDate

class FakeDailyStepRepo: DailyStepRepo {

    val dateToday: LocalDate = LocalDate.of(2026, 2, 21)
    private val goals =
        listOf(DailyStepGoal(
            YYYY = dateToday.year,
            MM = dateToday.monthValue,
            DD = dateToday.dayOfMonth,
            1000))

    private val goalObservable = MutableStateFlow<DailyStepGoal?>(null)
    private val _stepCount:MutableStateFlow<Map<Long,DailyStepCount>> = MutableStateFlow(mutableMapOf())

    private val _userStepCountOverride = MutableStateFlow<Map<Long, DailyStepCount>>(mutableMapOf())

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
        _stepCount.update {
            it.plus(stepCount.dayEpochDay to stepCount)
        }
    }

    override suspend fun getStepCountForDate(date: Long): DailyStepCount? {
        return _stepCount.value[date]
    }


    override suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount) {
        _baseline.update {
            dailyStepCount
        }
    }

    override suspend fun getDailyStepCountBaselineForDate(date: DateYYYYMMDD): DailyStepCount? {
        return _baseline.value
    }

    override suspend fun saveDailyStepCountUserOverride(
        dateYYYYMMDD: DateYYYYMMDD,
        stepCount: Int
    ) {
        val actual = getStepCountForDate(dateYYYYMMDD.dateEpochDay)?.stepCount ?: 0
        val new = DailyStepCountCreator.create(stepCount - actual, dateYYYYMMDD)

        println("--- FAKE DAILY STEP REPO --- saveDailyStepCountUserOverride: $new")
        _userStepCountOverride.update {
            it.plus(new.dayEpochDay to new)
        }
    }

    private fun getDailyStepCountUserOverrideForDay(date: Long): DailyStepCount? {
        return _userStepCountOverride.value[date]
    }

    var mergeCount = 0
    override val stepCountPlusUserOverride: Flow<List<DailyStepCount>>
        = merge(_userStepCountOverride, _stepCount).map {
           it.map { (day, dailyStepCount) ->
               val userOverride = getDailyStepCountUserOverrideForDay(day)?.stepCount ?: 0
               val actual = getStepCountForDate(day)?.stepCount ?: 0

               println("--- FAKE DAILY STEP REPO --- userOverride: $userOverride, actual: $actual")
               mergeCount ++
               println("--- FAKE DAILY STEP REPO --- mergeCount: $mergeCount")
               dailyStepCount.copy(stepCount = actual + userOverride)
           }
    }

    fun reset() {
        _baseline.update {
            null
        }
        _stepCount.update {
            mutableMapOf()
        }
        _userStepCountOverride.update {
            mutableMapOf()
        }
    }
}