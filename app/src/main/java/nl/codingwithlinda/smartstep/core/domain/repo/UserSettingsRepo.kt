package nl.codingwithlinda.smartstep.core.domain.repo

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits

interface UserSettingsRepo {
    suspend fun loadSettings(): UserSettings
    suspend fun saveSettings(settings: UserSettings)

    suspend fun saveUnitSystem(systemUnits: UnitSystemUnits)
    val unitSystemObservable : Flow<UnitSystemUnits>

    val userSettingsObservable : Flow<UserSettings>
    suspend fun skip()
    suspend fun loadSkip(): Boolean

    val skippedObservable : Flow<Boolean>

}