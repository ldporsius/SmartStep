package nl.codingwithlinda.smartstep.core.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.DailyStepCountDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.DailyStepGoalDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toDomain
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toEntity
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
            dailyStepGoal.toEntity(
                userId = userId
            )
        )
    }

    override fun getDailyStepGoals(): Flow<List<DailyStepGoal>> {
        return dailyStepGoalDao.getAllDailyStepGoals().map {
            it.map {
                it.toDomain()
            }
        }
    }

    override suspend fun getDailyStepGoalsForUser(): List<DailyStepGoal> {
        return dailyStepGoalDao.getAllDailyStepGoals().map {
            it.map {
                it.toDomain()
            }
        }.firstOrNull() ?: emptyList()
    }

    override suspend fun saveStepCount(stepCount: DailyStepCount) {
        val entity = stepCount.toEntity(userId)
        dailyStepCountDao.saveDailyStepCount(entity)
    }

    override val stepCount: Flow<DailyStepCount> =
        dailyStepCountDao.getDailyStepCount().map {list->
            list.map {
                it.toDomain()
            }
        }.mapNotNull {
            it.lastOrNull()
        }

}