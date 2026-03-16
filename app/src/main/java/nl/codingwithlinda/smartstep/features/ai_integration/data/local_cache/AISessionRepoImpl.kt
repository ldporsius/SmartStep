package nl.codingwithlinda.smartstep.features.ai_integration.data.local_cache

import androidx.compose.ui.util.fastJoinToString
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import kotlin.collections.take

class AISessionRepoImpl(
    private val dataStore: DataStore<Preferences>
): AISessionRepo {

    private val KEY_REQUESTS_MADE_MINUTE = stringPreferencesKey("requests_made_minute")
    private val KEY_REQUESTS_MADE_DAY = intPreferencesKey("requests_made_day")
    private val KEY_SESSION_TIMED_OUT = longPreferencesKey("session_timed_out")


    override suspend fun requestsMadeMinute(): List<Long>
      = dataStore.data.firstOrNull()?.get(KEY_REQUESTS_MADE_MINUTE)?.split(",")?.map { it.toLong() } ?: emptyList()

    override suspend fun saveRequestsMadeMinute(requestTimestampMillis: Long) {
        val timestamps = requestsMadeMinute().takeLast(60).plus(requestTimestampMillis)

        dataStore.edit {
            it[KEY_REQUESTS_MADE_MINUTE] = timestamps.fastJoinToString(",")
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