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
        lateinit var dataStoreSettings: DataStore<Preferences>
        lateinit var userSettingsRepo: UserSettingsRepo
        lateinit var dailyStepRepo: DailyStepRepo
        lateinit var walkDurationRepo: WalkDurationRepo
        lateinit var stepTracker: StepTracker
        lateinit var statisticsManager: StatisticsManager

        lateinit var _applicationContext: Context

        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        fun killAll(){
            Process.killProcess(Process.myPid());
        }
    }



    override fun onCreate() {
        super.onCreate()

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
            )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val db = SmartStepRoomDatabaseCreator.getInstance(this)
        dataStoreSettings = applicationContext.dataStore
        userSettingsRepo = PreferencesUserSettingsRepo(dataStoreSettings)
        dailyStepRepo = DailyStepRepoRoomImpl(
            dailyStepGoalDao = db.dailyStepGoalDao,
            dailyStepCountDao = db.dailyStepCountDao,
            userStepOverrideDao = db.userStepOverrideDao,
            userId = "todo"
        )

        walkDurationRepo = WalkDurationRepoImpl()

        stepTracker = StepTrackerDetectorImpl.getInstance(
            context = this.applicationContext,
            scope = applicationScope,
            repo = dailyStepRepo
        )
      /*  stepTracker = StepTrackerCounterImpl.getInstance(
            context = this.applicationContext,
            dailyStepRepo = dailyStepRepo
        )*/

        statisticsManager = StatisticsManagerImpl(
            userSettingsRepo = userSettingsRepo,
            dailyStepRepo = dailyStepRepo,
            walkDurationRepo = walkDurationRepo,
            dispatcherProvider = AndroidDispatcherProvider()
        )




        _applicationContext = this
        applicationScope.launch {
            userSettingsRepo.loadSettings().run {
                UserSettingsMemento.save(this)
            }
        }

        //debug to know what raw data the sensor contains
        StepTrackerCounterAdmin(
            this,
            StepTrackerAdminRepo(db.stepSensorCounterDao)
        ).start()
    }
}
