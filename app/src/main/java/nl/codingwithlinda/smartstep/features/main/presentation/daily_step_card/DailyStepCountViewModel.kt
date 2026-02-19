package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class DailyStepCountViewModel(
    dailyStepRepo: DailyStepRepo
): ViewModel() {

   private val userOverrides = dailyStepRepo.getDailyStepCountUserOverride()

   private val stepCount = dailyStepRepo.stepCount.map {
        it.stepCount
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val stepsToday = userOverrides.combine(stepCount){
        userOverrides, stepCount ->
        val userOverride = userOverrides.firstOrNull()?.stepCount ?: 0

        userOverride + stepCount
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


}