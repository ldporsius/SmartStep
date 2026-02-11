package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.model.settings.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits

class FakeUserSettingsRepo: UserSettingsRepo {

    private val settingsFlow = MutableStateFlow<UserSettings>(UserSettings())
    private val _unitSystemObservable = MutableStateFlow<UnitSystemUnits>(UnitSystemUnits.SI)
    override suspend fun loadSettings(): UserSettings {
        return settingsFlow.first()
    }

    override suspend fun saveSettings(settings: UserSettings) {
        settingsFlow.update {
            settings
        }
    }

    override suspend fun setIsOnboardingFalse() {

    }

    override suspend fun loadIsOnboarding(): Boolean {
        return false
    }

    override val isOnboardingObservable: Flow<Boolean>
        get() = flow {
            emit(false)
        }

    override val userSettingsObservable: Flow<UserSettings>
        get() = settingsFlow

    override suspend fun saveUnitSystem(systemUnits: UnitSystemUnits) {
       _unitSystemObservable.update {
           systemUnits
       }
    }

    override val unitSystemObservable: Flow<UnitSystemUnits>
    = _unitSystemObservable
}