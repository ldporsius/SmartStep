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
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepRoomDatabase
import nl.codingwithlinda.smartstep.core.data.repo.DailyStepRepoRoomImpl
import nl.codingwithlinda.smartstep.core.data.repo.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SmartStepApplication: Application() {

    companion object {
        lateinit var dataStoreSettings: DataStore<Preferences>
        lateinit var userSettingsRepo: UserSettingsRepo
        lateinit var dailyStepRepo: DailyStepRepo
        lateinit var stepTracker: StepTracker
        lateinit var _applicationContext: Context

        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        fun killAll(){
            stepTracker.stop()
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    override fun onCreate() {
        super.onCreate()
        val db = SmartStepRoomDatabase(this)
        dataStoreSettings = applicationContext.dataStore
        userSettingsRepo = PreferencesUserSettingsRepo(dataStoreSettings)
        dailyStepRepo = DailyStepRepoRoomImpl(
            dailyStepGoalDao = db.db.dailyStepGoalDao,
            dailyStepCountDao = db.db.dailyStepCountDao,
            userId = "todo"
        )

        stepTracker = StepTrackerImpl(
            context = this,
            scope = applicationScope
        )
        _applicationContext = this
        applicationScope.launch {
            userSettingsRepo.loadSettings().run {
                UserSettingsMemento.save(this)
            }
        }

    }

}