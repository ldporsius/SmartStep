package nl.codingwithlinda.smartstep.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepRoomDatabaseCreator
import nl.codingwithlinda.smartstep.core.data.repo.DailyStepRepoRoomImpl
import nl.codingwithlinda.smartstep.core.data.repo.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService.Companion.CHANNEL_ID
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SmartStepApplication: Application() {

    companion object {
        lateinit var dataStoreSettings: DataStore<Preferences>
        lateinit var userSettingsRepo: UserSettingsRepo
        lateinit var dailyStepRepo: DailyStepRepo
        //lateinit var stepTracker: StepTracker
        lateinit var _applicationContext: Context

        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        fun killAll(){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }



    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
                )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val db = SmartStepRoomDatabaseCreator.getInstance(this)
        dataStoreSettings = applicationContext.dataStore
        userSettingsRepo = PreferencesUserSettingsRepo(dataStoreSettings)
        dailyStepRepo = DailyStepRepoRoomImpl(
            dailyStepGoalDao = db.dailyStepGoalDao,
            dailyStepCountDao = db.dailyStepCountDao,
            userId = "todo"
        )


        _applicationContext = this
        applicationScope.launch {
            userSettingsRepo.loadSettings().run {
                UserSettingsMemento.save(this)
            }
        }


    }

}