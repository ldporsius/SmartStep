package nl.codingwithlinda.smartstep.core.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import nl.codingwithlinda.smartstep.core.domain.repo.AISessionRepo

class AISessionRepoImpl(
    private val dataStore: DataStore<Preferences>
): AISessionRepo {

    private val KEY_REQUESTS_MADE_MINUTE = intPreferencesKey("requests_made_minute")
    private val KEY_REQUESTS_MADE_DAY = intPreferencesKey("requests_made_day")
    private val KEY_SESSION_TIMED_OUT = longPreferencesKey("session_timed_out")

    private val KEY_HISTORY = stringPreferencesKey("AI_history")


    override val history = dataStore.data.map {
        it[KEY_HISTORY]?.split(",") ?: emptyList()
    }
    override suspend fun saveInHistory(message: String) {
        dataStore.edit {
            val history = it[KEY_HISTORY]?.split(",")?.toMutableList() ?: mutableListOf()
            history.add(message)
        }
    }

    override suspend fun requestsMadeMinute(): Int
      = dataStore.data.firstOrNull()?.get(KEY_REQUESTS_MADE_MINUTE) ?: 0


    override suspend fun saveRequestsMadeMinute(requests: Int) {
        dataStore.edit {
            it[KEY_REQUESTS_MADE_MINUTE] = requests
        }
    }

    override suspend fun requestsMadeDay(): Int =
        dataStore.data.firstOrNull()?.get(KEY_REQUESTS_MADE_DAY) ?: 0

    override suspend fun saveRequestsMadeDay(requests: Int) {
        dataStore.edit {
            it[KEY_REQUESTS_MADE_DAY] = requests
        }
    }

    override suspend fun sessionTimedOut(): Long =
        dataStore.data.firstOrNull()?.get(KEY_SESSION_TIMED_OUT) ?: System.currentTimeMillis()


    override suspend fun saveSessionTimedOut(time: Long) {
        dataStore.edit {
            it[KEY_SESSION_TIMED_OUT] = time
        }
    }
}