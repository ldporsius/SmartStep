package nl.codingwithlinda.smartstep.features.daily_step_count

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper

class DailyStepCountViewModel(
    dailyStepRepo: DailyStepRepo
): ViewModel() {

   private val stepCount = dailyStepRepo.stepCountPlusUserOverride

    private val today: DateYYYYMMDD
        get() = DateTimeHelper.toDateYYYYMMDD(System.currentTimeMillis())

    val todaysStep = stepCount.mapNotNull { dailyStepCounts ->
        dailyStepCounts.firstOrNull {
            it.dayEpochDay == today.dateEpochDay
        }
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)


    val stepsToday = dailyStepRepo.stepCountPlusUserOverride.map { dailyStepCounts ->
        dailyStepCounts.firstOrNull {
            it.dayEpochDay == today.dateEpochDay
        }?.stepCount ?:0
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), 0)



}