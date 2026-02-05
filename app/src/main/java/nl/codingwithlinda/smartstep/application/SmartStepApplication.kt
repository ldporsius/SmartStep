package nl.codingwithlinda.smartstep.application

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.data.repo.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SmartStepApplication: Application() {

    companion object {
        lateinit var dataStoreSettings: DataStore<Preferences>
        lateinit var userSettingsRepo: UserSettingsRepo

        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    }

    override fun onCreate() {
        super.onCreate()
        dataStoreSettings = applicationContext.dataStore
        userSettingsRepo = PreferencesUserSettingsRepo(dataStoreSettings)

       applicationScope.launch {
           userSettingsRepo.loadSettings().run {
               UserSettingsMemento.save(this)
           }
       }
    }

}