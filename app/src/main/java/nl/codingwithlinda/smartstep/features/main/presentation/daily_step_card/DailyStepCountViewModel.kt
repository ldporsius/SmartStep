package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class DailyStepCountViewModel(
    dailyStepRepo: DailyStepRepo
): ViewModel() {

   private val userOverrides = dailyStepRepo.getDailyStepCountUserOverride()

   private val stepCount = dailyStepRepo.stepCount

    val todaysStep = stepCount.mapNotNull{
        it.firstOrNull {
            it.dayEpochDay == DailyStepCountCreator.getTodayAsYYYYMMDD().dateEpochDay
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val stepsToday = userOverrides.combine(stepCount){ userOverrides, stepCount ->
        val today = DailyStepCountCreator.getTodayAsYYYYMMDD()
        val userOverride = userOverrides.firstOrNull{
            it.dayEpochDay == today.dateEpochDay
        }?.stepCount ?:0

        val stepsToday = stepCount.firstOrNull{
            it.dayEpochDay == today.dateEpochDay
        }?.stepCount ?: 0

        userOverride + stepsToday
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


}