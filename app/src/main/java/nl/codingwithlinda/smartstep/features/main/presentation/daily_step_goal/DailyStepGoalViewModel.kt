package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepGoalCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.stepGoalRange
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class DailyStepGoalViewModel(
    private val appScope: CoroutineScope,
    private val dailyStepRepo: DailyStepRepo,
    private val stepTracker: StepTracker
): ViewModel() {


    private val _goal = MutableStateFlow(1)
    val goal = _goal.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)


    val stepsTaken = dailyStepRepo.stepCount.map {
        it.stepCount
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


    init {
        appScope.launch {
            stepTracker.initialize()
            stepTracker.stepsTaken.collect {
                val update = stepsTaken.value + it
                dailyStepRepo.saveStepCount(DailyStepCountCreator.create(update))
            }
        }
        viewModelScope.launch {
            dailyStepRepo.getDailyStepGoalsForUser().also {
                val now = System.currentTimeMillis()
                val today =
                    DailyStepGoalCreator.getTodaysGoal(goals = it, today = now)?.goal
                        ?: stepGoalRange.first()

                _goal.update {
                    today
                }
            }
        }
    }
    fun setGoal(goal: Int){
        _goal.update {
            goal
        }
    }

    fun saveGoal(goal: Int){
        SmartStepApplication.applicationScope.launch {
            DailyStepGoalCreator.create(goal = goal).let {
                dailyStepRepo.saveDailyStepGoal(it)
            }
        }
    }


}