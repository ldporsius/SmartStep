package nl.codingwithlinda.smartstep.application.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import nl.codingwithlinda.smartstep.application.dataStore
import nl.codingwithlinda.smartstep.application.dataStoreAI
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepRoomDatabaseCreator
import nl.codingwithlinda.smartstep.features.ai_integration.data.local_cache.AISessionRepoImpl
import nl.codingwithlinda.smartstep.core.data.repo.ActivityRecognitionRepoImpl
import nl.codingwithlinda.smartstep.core.data.repo.DailyStepRepoRoomImpl
import nl.codingwithlinda.smartstep.core.data.repo.PreferencesUserSettingsRepo
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerDetectorImpl
import nl.codingwithlinda.smartstep.core.data.walk_duration.WalkDurationRepoImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.features.statistics.data.StatisticsManagerImpl
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager

class AppContainerImpl(
    private val context: Application
): AppContainer() {

    override val applicationWideScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    override val dataStoreSettings: DataStore<Preferences> = context.dataStore
    override val userSettingsRepo: UserSettingsRepo by lazy {
        PreferencesUserSettingsRepo(dataStoreSettings)
    }
    override val dailyStepRepo: DailyStepRepo by lazy {
        val db = SmartStepRoomDatabaseCreator.getInstance(context)

        DailyStepRepoRoomImpl(
            dailyStepGoalDao = db.dailyStepGoalDao,
            dailyStepCountDao = db.dailyStepCountDao,
            userStepOverrideDao = db.userStepOverrideDao,
            userId = "todo"
        )
    }
    override val walkDurationRepo: WalkDurationRepo by lazy {
        WalkDurationRepoImpl()
    }

    override val aiSessionRepo: AISessionRepo by lazy {
        AISessionRepoImpl(context.dataStoreAI)
    }

    private
    val activityRecognitionRepo = ActivityRecognitionRepoImpl(
        dailyStepCountDao = SmartStepRoomDatabaseCreator.getInstance(context).dailyStepCountDao,
        userId = "todo"
    )

    override
    val stepTracker: StepTracker by lazy {

        /*  stepTracker = StepTrackerCounterImpl.getInstance(
           context = this.applicationContext,
           dailyStepRepo = dailyStepRepo
       )*/
        StepTrackerDetectorImpl.getInstance(
            context = context,
            scope = applicationWideScope,
            repo = activityRecognitionRepo
        )
    }
    override val statisticsManager: StatisticsManager by lazy {
        StatisticsManagerImpl(
            userSettingsRepo = userSettingsRepo,
            dailyStepRepo = dailyStepRepo,
            walkDurationRepo = walkDurationRepo,
            dispatcherProvider = AndroidDispatcherProvider(),
            applicationScope = applicationWideScope
        )
    }


}