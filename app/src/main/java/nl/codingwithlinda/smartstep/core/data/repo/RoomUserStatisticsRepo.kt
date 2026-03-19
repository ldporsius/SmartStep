package nl.codingwithlinda.smartstep.core.data.repo

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.dao.StatisticsDao
import nl.codingwithlinda.smartstep.core.domain.model.settings.Gender
import nl.codingwithlinda.smartstep.core.domain.model.settings.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserStatisticsRepo

class RoomUserStatisticsRepo(
    private val dao: StatisticsDao
): UserStatisticsRepo {
    override suspend fun userSettingsForDay(dayEpoch: Long): UserSettings {
        return dao.getStatisticsForDay(dayEpoch)?.let {
            UserSettings(
                gender = Gender.valueOf(it.userGender),
                heightCm = it.userHeightCm,
                weightGrams = it.userWeightGrams
            )

        }?: UserSettings()

    }
}