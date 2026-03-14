package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepGoalCreator
import java.time.LocalDate

class FakeDailyStepRepo(
    val dateToday: LocalDate = LocalDate.now(),
    private val stepsTaken: Flow<List<DailyStepCount>>,
    private val getStepCountForDate: suspend (Long) -> DailyStepCount?
): DailyStepRepo {


    private val goals =
        listOf(DailyStepGoal(
            YYYY = dateToday.year,
            MM = dateToday.monthValue,
            DD = dateToday.dayOfMonth,
            1000))

    private val goalObservable = MutableStateFlow<DailyStepGoal?>(null)

    private val _userStepCountOverride =
        MutableStateFlow<Map<Long, DailyStepCount>>(mutableMapOf())


    private val userStepCountOverride = _userStepCountOverride.map {
        it.values.toList()
    }


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

    override suspend fun getGoalForDay(dateYYYYMMDD: DateYYYYMMDD): DailyStepGoal {
        return goals.find {
            it.epochDay == dateYYYYMMDD.dateEpochDay
        }?: DailyStepGoalCreator.create(
            goal = 1000,
            date = dateYYYYMMDD.dateEpochDay
        )
    }


    override suspend fun saveDailyStepCountUserOverride(
        dateYYYYMMDD: DateYYYYMMDD,
        stepCount: Int
    ) {
        val actual = getStepCountForDate(dateYYYYMMDD.dateEpochDay)?.stepCount ?: 0
        val new = DailyStepCountCreator.create(stepCount - actual, dateYYYYMMDD)

        println("--- FAKE DAILY STEP REPO --- saveDailyStepCountUserOverride: override = $stepCount , actual = $actual, new = $new")
        _userStepCountOverride.update {
            it.plus(new.dayEpochDay to new)
        }
    }

    private fun getDailyStepCountUserOverrideForDay(date: Long): DailyStepCount? {
        return _userStepCountOverride.value[date]
    }

    @OptIn(DelicateCoroutinesApi::class)
    override val stepCountPlusUserOverride: Flow<List<DailyStepCount>> = combine(stepsTaken, userStepCountOverride){ steps , userOverrides->
        steps.plus(userOverrides).groupBy {
            it.dayEpochDay
        }.map{ entry ->
            println("--- FAKE DAILY STEP REPO --- merged entry: $entry")
            val stepCount = entry.value.sumOf {
                it.stepCount }
            println("--- FAKE DAILY STEP REPO --- stepCountPlusUserOverride: $stepCount")
            DailyStepCountCreator.create(stepCount, entry.key)
        }
    }
    fun reset() {
        _baseline.update {
            null
        }

        _userStepCountOverride.update {
            mutableMapOf()
        }
    }
}