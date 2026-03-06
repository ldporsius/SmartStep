package nl.codingwithlinda.smartstep.application.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager

abstract class AppContainer{

    abstract val dataStoreSettings: DataStore<Preferences>
    abstract val userSettingsRepo: UserSettingsRepo
    abstract val dailyStepRepo: DailyStepRepo
    abstract val walkDurationRepo: WalkDurationRepo
    abstract val aiSessionRepo: AISessionRepo
    abstract val stepTracker: StepTracker
    abstract val statisticsManager: StatisticsManager

    abstract val applicationWideScope: CoroutineScope

}