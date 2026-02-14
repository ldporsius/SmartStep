package nl.codingwithlinda.smartstep.core.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import nl.codingwithlinda.smartstep.core.domain.model.settings.Gender
import nl.codingwithlinda.smartstep.core.domain.model.settings.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems

class PreferencesUserSettingsRepo(
    private val dataStore: DataStore<Preferences>
): UserSettingsRepo {

    val USER_SETTINGS_GENDER = stringPreferencesKey("user_gender")
    val USER_SETTINGS_WEIGHT = doublePreferencesKey("user_weight")
    val USER_SETTINGS_HEIGHT = intPreferencesKey("user_height")
    val USER_SETTINGS_ONBOARDING = booleanPreferencesKey("user_skip")
    val USER_SETTINGS_UNIT_SYSTEM = stringPreferencesKey("user_unit_system")

    override suspend fun loadSettings(): UserSettings {
        dataStore.data.firstOrNull() ?: return UserSettings()
        val settings = dataStore.data.first().let {
            val gender = it[USER_SETTINGS_GENDER]?.let { gender ->
                Gender.valueOf(gender)
            }?: Gender.FEMALE

            val weight = it[USER_SETTINGS_WEIGHT] ?: UserSettings().weightGrams
            val height = it[USER_SETTINGS_HEIGHT] ?: UserSettings().heightCm


            UserSettings(gender = gender, weightGrams = weight, heightCm = height)
            }
        return settings
    }

    override suspend fun saveSettings(settings: UserSettings) {
        dataStore.edit {
            it[USER_SETTINGS_GENDER] = settings.gender.name
            it[USER_SETTINGS_WEIGHT] = settings.weightGrams
            it[USER_SETTINGS_HEIGHT] = settings.heightCm
        }
    }

    override suspend fun setIsOnboardingFalse() {
        dataStore.edit {
            it[USER_SETTINGS_ONBOARDING] = false
        }
    }

    override suspend fun loadIsOnboarding(): Boolean {
        return dataStore.data.firstOrNull()?.get(
            USER_SETTINGS_ONBOARDING
        ) ?: return true

    }

    override val userSettingsObservable: Flow<UserSettings>
        get() = dataStore.data.map {
            val gender = it[USER_SETTINGS_GENDER]?.let { gender ->
                Gender.valueOf(gender)
            }?: Gender.FEMALE

            UserSettings(
                gender = gender,
                weightGrams = it[USER_SETTINGS_WEIGHT] ?: UserSettings().weightGrams,
                heightCm = it[USER_SETTINGS_HEIGHT] ?: UserSettings().heightCm

            )
        }

    override val isOnboardingObservable: Flow<Boolean>
        get() = dataStore.data.map {
            it[USER_SETTINGS_ONBOARDING] ?: true
        }

    override suspend fun saveUnitSystem(systemUnits: UnitSystems) {
        dataStore.edit {
            it[USER_SETTINGS_UNIT_SYSTEM] = systemUnits.toString()
        }
    }

    override val unitSystemObservable: Flow<UnitSystems>
       = dataStore.data.map {
           it[USER_SETTINGS_UNIT_SYSTEM] ?: UnitSystems.SI.toString()
       }.map {
          when(it){
              UnitSystems.SI.toString() -> UnitSystems.SI
              UnitSystems.IMPERIAL.toString() -> UnitSystems.IMPERIAL
              else -> UnitSystems.SI
          }
    }
}