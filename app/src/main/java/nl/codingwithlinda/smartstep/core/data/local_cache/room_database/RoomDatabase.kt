package nl.codingwithlinda.smartstep.core.data.local_cache.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.DailyStepCountDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.DailyStepGoalDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.SensorStepCounterDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.StatisticsDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.UserStepOverrideDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.WalkSessionDao
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountBaseline
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountUserOverride
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepGoalEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.SensorStepCounterData
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.StatisticsEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.WalkSessionEntity

@Database(
    entities = [
        DailyStepGoalEntity::class,
        DailyStepCountEntity::class,
        DailyStepCountBaseline::class,
        DailyStepCountUserOverride::class,
        SensorStepCounterData::class,
        WalkSessionEntity::class,
        StatisticsEntity::class
    ],
    version = 8
)
abstract class SmartStepDatabase: RoomDatabase() {

    abstract val dailyStepGoalDao: DailyStepGoalDao
    abstract val dailyStepCountDao: DailyStepCountDao
    abstract val userStepOverrideDao: UserStepOverrideDao

    abstract val stepSensorCounterDao: SensorStepCounterDao

    abstract val walkSessionDao: WalkSessionDao

    abstract val statisticsDao: StatisticsDao


    companion object{
        val databaseName = "nl.codingwithlinda.smartstep.database"
    }
}