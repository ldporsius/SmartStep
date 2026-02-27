package nl.codingwithlinda.smartstep.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Process
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.di.AndroidDispatcherProvider
import nl.codingwithlinda.smartstep.application.di.AppContainerImpl
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.ViewModelServiceLocator
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepRoomDatabaseCreator
import nl.codingwithlinda.smartstep.core.data.repo.DailyStepRepoRoomImpl
import nl.codingwithlinda.smartstep.core.data.repo.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerDetectorImpl
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService.Companion.CHANNEL_ID
import nl.codingwithlinda.smartstep.core.data.walk_duration.WalkDurationRepoImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.statistics.data.StatisticsManagerImpl
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import nl.codingwithlinda.smartstep.features.step_tracker_admin.data.StepTrackerAdminRepo
import nl.codingwithlinda.smartstep.features.step_tracker_admin.data.StepTrackerCounterAdmin

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SmartStepApplication: Application() {

    companion object {
        fun killAll(){
            Process.killProcess(Process.myPid());
        }
        lateinit var viewModelServiceLocator: ViewModelServiceLocator
        lateinit var statisticsManager: StatisticsManager

    }

    override fun onCreate() {
        super.onCreate()
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        val appContainer = AppContainerImpl(this)

        viewModelServiceLocator = ViewModelServiceLocator(appContainer)
        statisticsManager = appContainer.statisticsManager

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
            )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)


        applicationScope.launch {
            appContainer.userSettingsRepo.loadSettings().run {
                UserSettingsMemento.save(this)
            }
        }

        //debug to know what raw data the sensor contains
       /* StepTrackerCounterAdmin(
            this,
            StepTrackerAdminRepo(appContainer.db.stepSensorCounterDao)
        ).start()*/
    }
}
