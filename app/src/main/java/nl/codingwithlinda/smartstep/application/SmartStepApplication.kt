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
import nl.codingwithlinda.smartstep.application.di.AppContainerImpl
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.ViewModelServiceLocator
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService.Companion.CHANNEL_ID
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.dataStoreAI: DataStore<Preferences> by preferencesDataStore(name = "ai_session")

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
