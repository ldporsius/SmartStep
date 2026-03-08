package nl.codingwithlinda.smartstep.core.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.DailyStepCountDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.DailyStepGoalDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.UserStepOverrideDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toDomain
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toGoalEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toUserOverrideEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator

class DailyStepRepoRoomImpl(
    private val dailyStepGoalDao: DailyStepGoalDao,
    private val dailyStepCountDao: DailyStepCountDao,
    private val userStepOverrideDao: UserStepOverrideDao,
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

    override suspend fun getDailyStepGoalsLatest(): List<DailyStepGoal> {
        return dailyStepGoalDao.getAllDailyStepGoals().map { goalEntities ->
            goalEntities.map {
                it.toDomain()
            }
        }.firstOrNull() ?: emptyList()
    }

    override suspend fun getGoalForDay(dateYYYYMMDD: DateYYYYMMDD): DailyStepGoal? {
       return dailyStepGoalDao.getDailyStepGoal(dateYYYYMMDD.dateEpochDay)?.toDomain()
    }

    //////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun saveDailyStepCountUserOverride(
        dateYYYYMMDD: DateYYYYMMDD,
        stepCount: Int
    ) {
        /*
        Because we keep track of actual counts from the sensor and the user override in separate tables
        we need to make a calculation here. Otherwise, the actual values, over which we have no control, will interfere
        with the user override.
         */
        val current  = dailyStepCountDao.getDailyStepCountForDate(dateYYYYMMDD.dateEpochDay)?.stepCount ?: 0
        val override = stepCount - current
        val entity = DailyStepCountCreator.create(override, dateYYYYMMDD)
        userStepOverrideDao.saveDailyStepUserOverride(entity.toUserOverrideEntity())
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    private val stepCount = dailyStepCountDao.getDailyStepCount().map { entities ->
        entities.map {
            it.toDomain()
        }
    }

    private val userOverride = userStepOverrideDao.getDailyStepCountUserOverride().map {
        it.map { entity ->
            entity.toDomain()
        }
    }

    private val stepsTotalCombined = combine(stepCount, userOverride) { stepCounts, userOverrides ->
        stepCounts.plus(userOverrides)
            .groupBy { it.dayEpochDay }
            .mapValues {(dayEpochDay, dailyStepCounts) ->
                DailyStepCountCreator.create(
                    date = dayEpochDay,
                    count = dailyStepCounts.sumOf { it.stepCount }
                )
            }.values.toList()

    }

    override val stepCountPlusUserOverride: Flow<List<DailyStepCount>> = stepsTotalCombined


}