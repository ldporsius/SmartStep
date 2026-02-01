package nl.codingwithlinda.smartstep.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import nl.codingwithlinda.smartstep.core.domain.model.Gender
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo

class PreferencesUserSettingsRepo(
    private val dataStore: DataStore<Preferences>
): UserSettingsRepo {

    val USER_SETTINGS_GENDER = stringPreferencesKey("user_gender")
    val USER_SETTINGS_WEIGHT = intPreferencesKey("user_weight")
    val USER_SETTINGS_HEIGHT = intPreferencesKey("user_height")


    override suspend fun loadSettings(): UserSettings {
        dataStore.data.firstOrNull() ?: return UserSettings()
        val settings = dataStore.data.first().let {
            val gender = it[USER_SETTINGS_GENDER]?.let { gender ->
                Gender.valueOf(gender)
            }?: Gender.FEMALE

            val weight = it[USER_SETTINGS_WEIGHT] ?: UserSettings().weight
            val height = it[USER_SETTINGS_HEIGHT] ?: UserSettings().height


            UserSettings(gender, weight, height)
            }
        return settings
    }

    override suspend fun saveSettings(settings: UserSettings) {
        dataStore.edit {
            it[USER_SETTINGS_GENDER] = settings.gender.name
            it[USER_SETTINGS_WEIGHT] = settings.weight
            it[USER_SETTINGS_HEIGHT] = settings.height
        }
    }
}