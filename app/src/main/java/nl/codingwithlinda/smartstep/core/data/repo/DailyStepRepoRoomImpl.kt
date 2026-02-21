package nl.codingwithlinda.smartstep.core.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.DailyStepCountDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.DailyStepGoalDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toDomain
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toBaselineEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toGoalEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toUserOverrideEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class DailyStepRepoRoomImpl(
    private val dailyStepGoalDao: DailyStepGoalDao,
    private val dailyStepCountDao: DailyStepCountDao,
    private var userId: String
): DailyStepRepo {

    override suspend fun saveDailyStepGoal(dailyStepGoal: DailyStepGoal) {
        dailyStepGoalDao.upsertDailyStepGoal(
            dailyStepGoal.toGoalEntity(
                userId = userId
            )
        )
    }

    override fun getDailyStepGoals(): Flow<List<DailyStepGoal>> {
        return dailyStepGoalDao.getAllDailyStepGoals().map { goalEntities ->
            goalEntities.map {
                it.toDomain()
            }
        }
    }

    override suspend fun getDailyStepGoalsForUser(): List<DailyStepGoal> {
        return dailyStepGoalDao.getAllDailyStepGoals().map { goalEntities ->
            goalEntities.map {
                it.toDomain()
            }
        }.firstOrNull() ?: emptyList()
    }

    override suspend fun saveStepCount(stepCount: DailyStepCount) {
        stepCount.toEntity(userId).let {
            dailyStepCountDao.saveDailyStepCount(it)
        }
    }

    override suspend fun addStepCountToToday(stepCount: DailyStepCount){
        val entity =
            stepCount.toEntity(userId)

        dailyStepCountDao.saveDailyStepCount(entity)
    }

    override suspend fun getStepCountForDate(date: Long): DailyStepCount? {
        return dailyStepCountDao.getDailyStepCount().firstOrNull()?.let { entities ->
            entities.firstOrNull{
                        it.date == date
                    }?.toDomain()
        }
    }

    override val stepCount: Flow<List<DailyStepCount>> =
        dailyStepCountDao.getDailyStepCount().map {list->
            list.map {
                it.toDomain()
            }
        }

    override suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount) {
        dailyStepCountDao.saveDailyStepCountBaseline(dailyStepCount.toBaselineEntity())
    }

    override suspend fun getDailyStepCountBaselineForDate(date: Long): DailyStepCount? {
        return dailyStepCountDao.getDailyStepCountBaselineForDate(date)?.toDomain()
    }

    override suspend fun saveDailyStepCountUserOverride(dailyStepCount: DailyStepCount) {
        dailyStepCountDao.saveDailyStepCountUserOverride(dailyStepCount.toUserOverrideEntity())
    }

    override suspend fun getDailyStepCountUserOverrideForDay(date: Long): DailyStepCount? {
        return dailyStepCountDao.getDailyStepGoalUserOverrideForDay(date)?.toDomain()
    }

    override fun getDailyStepCountUserOverride(): Flow<List<DailyStepCount>> {
        return dailyStepCountDao.getDailyStepCountUserOverride().map {
            it.map {
                it.toDomain()
            }
        }
    }

}