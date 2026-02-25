package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.SensorStepCounterData


@Dao
interface SensorStepCounterDao {

    @Upsert
    suspend fun saveSensorStepCounterData(sensorStepCounterData: SensorStepCounterData)

    @Query("SELECT * FROM SensorStepCounterData")
    suspend fun getSensorStepCounterData(): List<SensorStepCounterData>

}