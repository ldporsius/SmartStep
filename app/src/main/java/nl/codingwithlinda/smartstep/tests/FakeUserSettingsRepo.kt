package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits

class FakeUserSettingsRepo: UserSettingsRepo {

    private val _unitSystemObservable = MutableStateFlow<UnitSystemUnits>(UnitSystemUnits.SI)
    override suspend fun loadSettings(): UserSettings {
        return UserSettings()
    }

    override suspend fun saveSettings(settings: UserSettings) {

    }

    override suspend fun skip() {

    }

    override suspend fun loadSkip(): Boolean {
        return false
    }

    override val skippedObservable: Flow<Boolean>
        get() = flow {
            emit(false)
        }

    override val userSettingsObservable: Flow<UserSettings>
        get() = flow {
            emit(UserSettings())
        }

    override suspend fun saveUnitSystem(systemUnits: UnitSystemUnits) {
       _unitSystemObservable.update {
           systemUnits
       }
    }

    override val unitSystemObservable: Flow<UnitSystemUnits>
    = _unitSystemObservable
}