package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StatisticsEntity(
    @PrimaryKey
    val dayEpoch: Long,
    val userHeightCm: Int,
    val userGender: String,
    val userWeightGrams: Double
)
