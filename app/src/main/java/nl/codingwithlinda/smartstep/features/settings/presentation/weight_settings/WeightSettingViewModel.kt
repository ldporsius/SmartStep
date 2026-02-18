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
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.GRAM
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.GramsWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.KGWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.LBS
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.LBSWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.Weights
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.convertWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.fromPreviousPounds
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState
import kotlin.math.roundToInt

class WeightSettingViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val memento: UserSettingsMemento,
): ViewModel(){
    private val _weightInputGrams = MutableStateFlow(GramsWeight(0))
    private val _weightInputPounds = MutableStateFlow(LBSWeight(0))

    private val system = userSettingsRepo.unitSystemObservable


    val weightUiState = combine(system, _weightInputGrams, _weightInputPounds) { system, grams, pounds->

        when (system) {
            is UnitSystems.SI -> {
                WeightSettingUiState.SI(grams.weight)
            }
            is UnitSystems.IMPERIAL -> WeightSettingUiState.Imperial(pounds)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeightSettingUiState.SI(0))


    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {

                val weightGrams = GramsWeight(it.weightGrams.roundToInt())
                _weightInputGrams.update {
                   weightGrams
                }
                val weightPounds = convertWeight(weightGrams, LBS)
                _weightInputPounds.update {
                    weightPounds as LBSWeight
                }
            }
        }
    }
    fun onAction(action: ActionWeightInput) {
        when (action) {
            is ActionWeightInput.KgInput -> {
                println("--- ActionWeightInput.KgInput --- kg: ${action.kg}")

                val kg = KGWeight(action.kg)
                val convertedToGrams = convertWeight(kg, GRAM)
                println("--- ActionWeightInput.KgInput --- converted: $convertedToGrams")

                _weightInputGrams.update {
                    convertedToGrams as GramsWeight
                }
                _weightInputPounds.update {
                    fromPreviousPounds(kg,_weightInputPounds.value.weight)
                }
            }

            is ActionWeightInput.PoundsInput -> {
                println("--- ActionWeightInput.PoundsInput --- pounds: ${action.pounds} ")

                val pounds = LBSWeight(action.pounds)
                val convertedToGrams = convertWeight(pounds, GRAM) as GramsWeight

                println("--- ActionWeightInput.PoundsInput --- convertedToGrams: $convertedToGrams")

                _weightInputGrams.update {
                    convertedToGrams
                }
                _weightInputPounds.update {
                    pounds
                }

            }

            is ActionWeightInput.Save -> {
                viewModelScope.launch(NonCancellable) {
                    val currentWeight = _weightInputGrams.value.weight.toDouble()
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