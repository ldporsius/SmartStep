package nl.codingwithlinda.smartstep.features.steps.edit.presentation

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
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction
import nl.codingwithlinda.smartstep.features.steps.domain.mapping.toDomain
import nl.codingwithlinda.smartstep.features.steps.domain.model.DatePicker
import nl.codingwithlinda.smartstep.features.steps.domain.model.DateYYYYMMDD
import nl.codingwithlinda.smartstep.features.steps.domain.model.months
import nl.codingwithlinda.smartstep.features.steps.domain.model.years
import nl.codingwithlinda.smartstep.features.steps.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.features.steps.navigation.StepNavActionHandler

class EditStepsViewModel(
    private val dailyStepRepo: DailyStepRepo
): ViewModel() {
    private val _steps = MutableStateFlow(0)
    val steps = _steps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


    val yearRange = years.toList()
    val monthRange = months.toList()

    private val _dateYYYYMMDD = MutableStateFlow(DateYYYYMMDD(years.first, months.first, 1))
    val dateYYYYMMDD = _dateYYYYMMDD.asStateFlow()

    val dayRange = _dateYYYYMMDD.mapNotNull {
        DatePicker(it.YYYY).daysInMonth(it.MM).toList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dateSelectedAsString = _dateYYYYMMDD
        .map {
            "${it.YYYY}/${it.MM}/${it.DD}"
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    init {
        viewModelScope.launch {
            dailyStepRepo.stepCount.firstOrNull()?.let {count ->

                _steps.update {
                    count.stepCount
                }
                val converted = DailyStepCountCreator.toDateYYYYMMDD(count.date)

                println("--- EDITSTEPS VIEWMODEL INIT --- converted step count to YYYYMMDD: $converted")

                _dateYYYYMMDD.update {
                    converted
                }
            }
        }

    }

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
                val asInt = action.steps.toIntOrNull() ?: -1000000

                    _steps.update {
                        asInt

                }
            }

            EditStepAction.Save -> {
                viewModelScope.launch {
                    val yyyyMMDDToDomain = _dateYYYYMMDD.value.toDomain()
                    println("--- EDITSTEPS VIEWMODEL SAVE DATE --- date to domain $yyyyMMDDToDomain")

                    val update = DailyStepCountCreator.create(
                        _steps.value,
                        _dateYYYYMMDD.value.toDomain()
                    )
                    println("--- EDITSTEPS VIEWMODEL SAVE DATE --- $update")
                    dailyStepRepo.saveStepCount(
                        update
                    )
                    StepNavActionHandler.handleAction(StepNavAction.NA)
                }
            }
        }
    }

}