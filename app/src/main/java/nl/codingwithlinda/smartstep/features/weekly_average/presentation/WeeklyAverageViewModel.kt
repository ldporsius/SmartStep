package nl.codingwithlinda.smartstep.features.weekly_average.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.stepGoalRange
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepGoalCreator
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.features.weekly_average.presentation.model.DailyAverageUi

class WeeklyAverageViewModel(
    repo: DailyStepRepo
): ViewModel() {


    val lastSevenStepCounts = repo.stepCount.map {
        it.takeLast(7)
    }.combine(repo.getDailyStepGoals()){counts, goals ->
        counts.map { step ->
            DailyAverageUi(
                stepCount = step,
                goal = goals.find {
                    it.epochDay == step.dayEpochDay
                } ?: DailyStepGoalCreator.create(stepGoalRange.first(), step.dayEpochDay)
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



}