package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.settings.UserSettings
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems

interface UserSettingsRepo {
    suspend fun loadSettings(): UserSettings
    suspend fun saveSettings(settings: UserSettings)

    suspend fun saveUnitSystem(systemUnits: UnitSystems)
    val unitSystemObservable : Flow<UnitSystems>

    val userSettingsObservable : Flow<UserSettings>
    suspend fun setIsOnboardingFalse()
    suspend fun loadIsOnboarding(): Boolean

    val isOnboardingObservable : Flow<Boolean>

}