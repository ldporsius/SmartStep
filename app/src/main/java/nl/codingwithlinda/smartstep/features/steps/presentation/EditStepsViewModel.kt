package nl.codingwithlinda.smartstep.features.steps.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.features.steps.domain.mapping.toDateYYYYMMDD
import nl.codingwithlinda.smartstep.features.steps.domain.model.DateYYYYMMDD
import nl.codingwithlinda.smartstep.features.steps.presentation.state.EditStepAction

class EditStepsViewModel(
    private val dailyStepRepo: DailyStepRepo
): ViewModel() {
    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow()


    private val dateYYYYMMDD = MutableStateFlow(DateYYYYMMDD(0, 0, 0))

    val dateSelected = dateYYYYMMDD
        .map {
            "${it.YYYY}/${it.MM}/${it.DD}"
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    init {
        viewModelScope.launch {
            dailyStepRepo.stepCount.first().let {count ->
                dateYYYYMMDD.update {
                    count.toDateYYYYMMDD()
                }
            }
        }

    }

    fun onAction(action: EditStepAction){
        when(action){
            is EditStepAction.InputDay -> {

            }
            is EditStepAction.InputMonth -> {}
            is EditStepAction.InputYear -> {}
            is EditStepAction.SetSteps -> {
                _steps.update {
                    action.steps.toInt()
                }
            }
        }
    }

}