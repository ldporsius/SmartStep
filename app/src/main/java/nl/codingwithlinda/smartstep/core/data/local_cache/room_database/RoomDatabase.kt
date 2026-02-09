package nl.codingwithlinda.smartstep.core.data.local_cache.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepCountEntity
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepGoalEntity

@Database(
    entities = [DailyStepGoalEntity::class, DailyStepCountEntity::class],
    version = 2
)
abstract class SmartStepDatabase: RoomDatabase() {

    abstract val dailyStepGoalDao: DailyStepGoalDao
    abstract val dailyStepCountDao: DailyStepCountDao


    companion object{
        val databaseName = "nl.codingwithlinda.smartstep.database"
    }
}