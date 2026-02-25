package nl.codingwithlinda.smartstep.core.data.local_cache.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountBaseline
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountUserOverride
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepGoalEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.SensorStepCounterData

@Database(
    entities = [
        DailyStepGoalEntity::class,
        DailyStepCountEntity::class,
        DailyStepCountBaseline::class,
        DailyStepCountUserOverride::class,
        SensorStepCounterData::class
    ],
    version = 5
)
abstract class SmartStepDatabase: RoomDatabase() {

    abstract val dailyStepGoalDao: DailyStepGoalDao
    abstract val dailyStepCountDao: DailyStepCountDao

    abstract val stepSensorCounterDao: SensorStepCounterDao


    companion object{
        val databaseName = "nl.codingwithlinda.smartstep.database"
    }
}