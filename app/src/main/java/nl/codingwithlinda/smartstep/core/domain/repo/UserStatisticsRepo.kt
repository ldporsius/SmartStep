package nl.codingwithlinda.smartstep.core.domain.repo

import nl.codingwithlinda.smartstep.core.domain.model.settings.UserSettings

interface UserStatisticsRepo {
    suspend fun userSettingsForDay(dayEpoch: Long): UserSettings
}