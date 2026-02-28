package nl.codingwithlinda.smartstep.core.domain.repo

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD

interface ActivityRecognitionRepo {

    suspend fun saveStepCount(stepCount: DailyStepCount)
    suspend fun getStepCountForDate(date: Long): DailyStepCount?

    /////////////////////////////////////////////////////////////////////
    suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount)
    suspend fun getDailyStepCountBaselineForDate(date: DateYYYYMMDD): DailyStepCount?

}