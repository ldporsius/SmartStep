package nl.codingwithlinda.smartstep.application

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SmartStepApplication: Application() {

    companion object {
        lateinit var dataStoreSettings: DataStore<Preferences>
    }

    override fun onCreate() {
        super.onCreate()
        dataStoreSettings = applicationContext.dataStore

    }

}