package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.Weights
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
    private val _weightInputGrams = MutableStateFlow(WeightUnits.Grams(0))

    private val system = userSettingsRepo.unitSystemObservable


    val weightUiState = combine(system, _weightInputGrams) { system, grams->

        when (system) {
            is UnitSystems.SI -> {
                WeightSettingUiState.SI(grams.grams)
            }
            is UnitSystems.IMPERIAL -> WeightSettingUiState.Imperial(grams.grams)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeightSettingUiState.SI(0))


    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {

                val weightGrams = it.weightGrams
                _weightInputGrams.update {
                    WeightUnits.Grams(weightGrams.toInt())
                }
            }
        }
    }
    fun onAction(action: ActionWeightInput) {
        when (action) {
            is ActionWeightInput.KgInput -> {
                println("--- ActionWeightInput.KgInput --- kg: ${action.kg}")

                val kg = WeightUnits.KG(action.kg)
                val convertedToGrams = kg.convert<WeightUnits.Grams>(Weights.GRAMS)

                println("--- ActionWeightInput.KgInput --- converted: $convertedToGrams")

                _weightInputGrams.update {
                    convertedToGrams
                }
            }

            is ActionWeightInput.PoundsInput -> {
                println("--- ActionWeightInput.PoundsInput --- pounds: ${action.pounds} ")

                val pounds = WeightUnits.LBS(action.pounds)
                val convertedToGrams = pounds.convert<WeightUnits.Grams>(Weights.GRAMS)

                println("--- ActionWeightInput.PoundsInput --- convertedToGrams: $convertedToGrams")

                _weightInputGrams.update {
                    convertedToGrams
                }

            }

            is ActionWeightInput.Save -> {
                viewModelScope.launch(NonCancellable) {
                    val currentWeight = _weightInputGrams.value.grams.toDouble()
                    val userSettings = memento.restoreLast().copy(weightGrams = currentWeight)
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