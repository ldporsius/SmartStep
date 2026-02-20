package nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction
import nl.codingwithlinda.smartstep.features.steps_override_user.domain.model.DatePicker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.features.steps_override_user.domain.model.months
import nl.codingwithlinda.smartstep.features.steps_override_user.domain.model.years
import nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.features.steps_override_user.navigation.StepNavActionHandler

class EditStepsViewModel(
    private val dailyStepRepo: DailyStepRepo
): ViewModel() {
    private val _steps = MutableStateFlow(0)

    private val _dateYYYYMMDD = MutableStateFlow(DateYYYYMMDD(years.first, months.first, 1))

    val steps = _steps
        .onStart {
            dailyStepRepo.stepCount.firstOrNull()?.let { fromRepo->
                _steps.update {
                   fromRepo.stepCount
                }

                _dateYYYYMMDD.update {
                    DateYYYYMMDD(fromRepo.YYYY, fromRepo.MM, fromRepo.DD)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


    val yearRange = years.toList()
    val monthRange = months.toList()


    val dateYYYYMMDD = _dateYYYYMMDD.asStateFlow()

    val dayRange = _dateYYYYMMDD.mapNotNull {
        DatePicker(it.YYYY).daysInMonth(it.MM).toList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dateSelectedAsString = _dateYYYYMMDD
        .map {
            "${it.YYYY}/${it.MM}/${it.DD}"
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")


    fun onAction(action: EditStepAction){
        when(action){
            is EditStepAction.DismissDatePicker -> {
                StepNavActionHandler.handleAction(StepNavAction.EDIT_STEPS)
            }
            is EditStepAction.ShowDatePicker -> {
                StepNavActionHandler.handleAction(StepNavAction.SHOW_DATE_PICKER)
            }
            is EditStepAction.InputDay -> {
                if (action.day < 1) return
                println("--- EDITSTEPS VIEWMODEL INPUT DAY --- ${action.day}")
                _dateYYYYMMDD.update {
                    it.copy(DD = action.day)
                }
            }
            is EditStepAction.InputMonth -> {
                _dateYYYYMMDD.update {
                    it.copy(MM = action.month)
                }
            }
            is EditStepAction.InputYear -> {
                _dateYYYYMMDD.update {
                    it.copy(YYYY = action.year)
                }
            }
            is EditStepAction.SetSteps -> {
                println("--- EDITSTEPS VIEWMODEL SET STEPS --- ${action.steps}")
                val asInt = action.steps.toIntOrNull() ?: 0
                    _steps.update {
                        asInt
                }
            }

            EditStepAction.Save -> {
                viewModelScope.launch {

                    val update = DailyStepCountCreator.create(
                        _steps.value,
                        _dateYYYYMMDD.value
                    )
                    println("--- EDITSTEPS VIEWMODEL SAVE DATE --- $update")
                    dailyStepRepo.saveDailyStepCountUserOverride(update)
                    StepNavActionHandler.handleAction(StepNavAction.NA)
                }
            }
        }
    }

}