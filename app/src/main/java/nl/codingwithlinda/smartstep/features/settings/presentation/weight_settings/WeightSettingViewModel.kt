package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.WeightUnitConverter
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState

class WeightSettingViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val memento: UserSettingsMemento,
    private val weightUnitConverter: WeightUnitConverter
): ViewModel(){
    private val _weightInput = MutableStateFlow(0)

    private val system = userSettingsRepo.unitSystemObservable

    val weightUiState = _weightInput.combine(system) { input, system ->
        when (system) {
            is UnitSystemUnits.SI -> WeightSettingUiState.SI(input)
            is UnitSystemUnits.IMPERIAL -> WeightSettingUiState.Imperial(input)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeightSettingUiState.SI(0))


    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {
                _weightInput.value = it.weight
            }
        }
    }
    fun onAction(action: ActionWeightInput) {
        when (action) {
            is ActionWeightInput.KgInput -> {
                _weightInput.value = action.kg
            }

            is ActionWeightInput.PoundsInput -> {
                _weightInput.value = weightUnitConverter.toSI(action.pounds)
            }

            is ActionWeightInput.Save -> {
                viewModelScope.launch(NonCancellable) {
                    val currentWeight = _weightInput.value
                    val userSettings = memento.restoreLast().copy(weight = currentWeight)
                    memento.save(userSettings)
                }
            }
            is ActionWeightInput.ChangeSystem -> {
                viewModelScope.launch(NonCancellable) {
                    userSettingsRepo.saveUnitSystem(action.system)
                }
            }
        }
    }
}