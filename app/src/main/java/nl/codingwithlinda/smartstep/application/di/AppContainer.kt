package nl.codingwithlinda.smartstep.application.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.features.ai_integration.di.AIContainer
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager

interface AppContainer{

    val dataStoreSettings: DataStore<Preferences>
    val userSettingsRepo: UserSettingsRepo
    val dailyStepRepo: DailyStepRepo
    val walkDurationRepo: WalkDurationRepo
    val aiSessionRepo: AISessionRepo
    val stepTracker: StepTracker
    val statisticsManager: StatisticsManager


    val applicationWideScope: CoroutineScope

    val AIContainer: AIContainer
}