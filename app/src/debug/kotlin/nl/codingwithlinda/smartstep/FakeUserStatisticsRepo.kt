package nl.codingwithlinda.smartstep

import nl.codingwithlinda.smartstep.core.domain.model.settings.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserStatisticsRepo

class FakeUserStatisticsRepo: UserStatisticsRepo {
    override suspend fun userSettingsForDay(dayEpoch: Long): UserSettings {
        return UserSettings()
    }
}