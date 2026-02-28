package nl.codingwithlinda.smartstep.core.data.repo

import kotlinx.coroutines.flow.firstOrNull
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.DailyStepCountDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toBaselineEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toDomain
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.repo.ActivityRecognitionRepo

class ActivityRecognitionRepoImpl(
    private val dailyStepCountDao: DailyStepCountDao,
    private val userId: String
): ActivityRecognitionRepo {

    override suspend fun saveStepCount(stepCount: DailyStepCount) {
        stepCount.toEntity(userId).let {
            dailyStepCountDao.saveDailyStepCount(it)
        }
    }

    override suspend fun getStepCountForDate(date: Long): DailyStepCount? {
        return dailyStepCountDao.getDailyStepCount().firstOrNull()?.let { entities ->
            entities.firstOrNull{
                it.date == date
            }?.toDomain()
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount) {
        dailyStepCountDao.saveDailyStepCountBaseline(dailyStepCount.toBaselineEntity())
    }

    override suspend fun getDailyStepCountBaselineForDate(date: DateYYYYMMDD): DailyStepCount? {
        return dailyStepCountDao.getDailyStepCountBaselineForDate(date.dateEpochDay)?.toDomain()
    }
}