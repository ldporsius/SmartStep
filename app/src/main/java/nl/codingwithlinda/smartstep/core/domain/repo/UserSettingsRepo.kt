package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings

interface UserSettingsRepo {
    suspend fun loadSettings(): UserSettings
    suspend fun saveSettings(settings: UserSettings)

    suspend fun skip()
    suspend fun loadSkip(): Boolean

    val skippedObservable : Flow<Boolean>

}