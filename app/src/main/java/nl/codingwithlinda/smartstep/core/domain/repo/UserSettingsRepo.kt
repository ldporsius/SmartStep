package nl.codingwithlinda.smartstep.core.domain.repo

import nl.codingwithlinda.smartstep.core.domain.model.UserSettings

interface UserSettingsRepo {
    suspend fun loadSettings(): UserSettings
    suspend fun saveSettings(settings: UserSettings)

}