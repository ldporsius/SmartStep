package nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.stepGoalRange
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepGoalCreator
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation.model.DailyAverageUi
import java.time.LocalDate

class WeeklyAverageViewModel(
    repo: DailyStepRepo
): ViewModel() {

    val now: LocalDate
        get() = LocalDate.now()
    val pastWeek = (-6 .. 0).map{
        now.plusDays(it.toLong())
    }.map {
        DateTimeHelper.toDateYYYYMMDD(it.toEpochDay())
    }

    val lastSevenStepCounts = repo.stepCountPlusUserOverride.map{steps ->
       pastWeek.map { weekday ->
           steps.find {
               it.dayEpochDay == weekday.dateEpochDay
           } ?: DailyStepCountCreator.create(0, weekday.dateEpochDay)
       }
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