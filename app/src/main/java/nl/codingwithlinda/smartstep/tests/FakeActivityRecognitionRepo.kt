package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.repo.ActivityRecognitionRepo

class FakeActivityRecognitionRepo: ActivityRecognitionRepo {

    private val _stepCount:MutableStateFlow<Map<Long,DailyStepCount>> = MutableStateFlow(mutableMapOf())
    private val _baseline = MutableStateFlow<DailyStepCount?>(null)

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
}