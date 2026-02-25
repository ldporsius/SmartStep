package nl.codingwithlinda.smartstep.core.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.DailyStepCountDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.DailyStepGoalDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.UserStepOverrideDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toBaselineEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toDomain
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.toEntity
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

    //////////////////////////////////////////////////////////////////////////////////
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

    private val stepCount: Flow<List<DailyStepCount>> =
        dailyStepCountDao.getDailyStepCount()
            .map {list->
                list.map {
                    it.toDomain()
                }
            }

    ////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun saveDailyStepCountBaseline(dailyStepCount: DailyStepCount) {
        dailyStepCountDao.saveDailyStepCountBaseline(dailyStepCount.toBaselineEntity())
    }

    override suspend fun getDailyStepCountBaselineForDate(date: DateYYYYMMDD): DailyStepCount? {
        return dailyStepCountDao.getDailyStepCountBaselineForDate(date.dateEpochDay)?.toDomain()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun saveDailyStepCountUserOverride(
        dateYYYYMMDD: DateYYYYMMDD,
        stepCount: Int
    ) {
        val current  = getStepCountForDate(dateYYYYMMDD.dateEpochDay)?.stepCount ?: 0
        val override = stepCount - current
        val entity = DailyStepCountCreator.create(override, dateYYYYMMDD)
        userStepOverrideDao.saveDailyStepUserOverride(entity.toUserOverrideEntity())
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    private val stepsTotal = dailyStepCountDao.getDailyStepCount()
        .map {
            it.map {
                it.toDomain()
            }
        }.combine(userStepOverrideDao.getDailyStepCountUserOverride()){steps, override ->
            steps.associateWith { step ->
                override.find { override ->
                    override.dateEpochDay == step.dayEpochDay
                }.let {
                    it?.stepCount ?: 0
                }
            }.map { (step, override) ->
                step.let { step ->
                    DailyStepCount(
                        YYYY = step.YYYY,
                        MM = step.MM,
                        DD = step.DD,
                        stepCount = step.stepCount + override
                    )
                }
            }
        }

    override val stepCountPlusUserOverride: Flow<List<DailyStepCount>>
        = stepsTotal

}