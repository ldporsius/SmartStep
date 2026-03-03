package nl.codingwithlinda.smartstep.features.daily_step_goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepGoalCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.stepGoalRange
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class DailyStepGoalViewModel(
    private val appScope: CoroutineScope,
    private val dailyStepRepo: DailyStepRepo,

): ViewModel() {
    private val _goal = MutableStateFlow(1)
    val goal = _goal.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    init {
        viewModelScope.launch {
            dailyStepRepo.getDailyStepGoalsLatest().also {
                val now = System.currentTimeMillis()
                val existingGoal =
                    DailyStepGoalCreator.getTodaysGoal(goals = it, today = now)

                if(existingGoal == null){
                    val today = DailyStepGoalCreator.create(goal = stepGoalRange.first())
                    dailyStepRepo.saveDailyStepGoal(today)
                    _goal.update {
                        today.goal
                    }
                }else
                    _goal.update {
                        existingGoal.goal
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
        appScope.launch {
            DailyStepGoalCreator.create(goal = goal).let {
                dailyStepRepo.saveDailyStepGoal(it)
            }
        }
    }

    fun dismissChanges(){
        viewModelScope.launch {
            _goal.update {
                dailyStepRepo.getDailyStepGoalsLatest().first().goal
            }
        }
    }
}