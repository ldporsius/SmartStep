package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model

import androidx.room.Entity

@Entity(
    primaryKeys = ["yearLastBoot", "monthLastBoot", "dayLastBoot"]
)
data class SensorStepCounterData(
    val yearLastBoot: Int,
    val monthLastBoot: Int,
    val dayLastBoot: Int,
    val timeStampSinceBoot: Long,
    val numberSteps: Long
)
