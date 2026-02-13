package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.WeightUnitConverter
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState
import kotlin.math.roundToInt

class WeightSettingViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val memento: UserSettingsMemento,
    private val weightUnitConverter: WeightUnitConverter
): ViewModel(){
    private val _weightInputKg = MutableStateFlow(0.0)
    private val _weightInputPounds = MutableStateFlow(0.0)

    private val system = userSettingsRepo.unitSystemObservable

    val weightUiState = _weightInputKg.combine(system) { input, system ->

        when (system) {
            is UnitSystemUnits.SI -> {
                WeightSettingUiState.SI(input.roundToInt())
            }
            is UnitSystemUnits.IMPERIAL -> WeightSettingUiState.Imperial(input)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeightSettingUiState.SI(0))


    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {
                _weightInputKg.value = it.weight
            }
        }
    }
    fun onAction(action: ActionWeightInput) {
        when (action) {
            is ActionWeightInput.KgInput -> {
                println("--- ActionWeightInput.KgInput --- kg: ${action.kg}")
                _weightInputKg.value = action.kg.toDouble()
            }

            is ActionWeightInput.PoundsInput -> {
                val converted = weightUnitConverter.convert(
                    value = action.pounds.toDouble(),
                    from = UnitSystemUnits.IMPERIAL,
                    to = WeightUnits.KG
                )
                println("--- ActionWeightInput.PoundsInput --- converted: ${action.pounds} to $converted")
                _weightInputKg.update {
                    converted
                }
            }

            is ActionWeightInput.Save -> {
                viewModelScope.launch(NonCancellable) {
                    val currentWeight = _weightInputKg.value
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