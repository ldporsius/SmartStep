package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.fromPreviousPounds
import nl.codingwithlinda.unit_conversion.domain.UnitSystems
import nl.codingwithlinda.unit_conversion.data.weight.GRAM
import nl.codingwithlinda.unit_conversion.data.weight.GramsWeight
import nl.codingwithlinda.unit_conversion.data.weight.KG
import nl.codingwithlinda.unit_conversion.data.weight.KGWeight
import nl.codingwithlinda.unit_conversion.data.weight.LBS
import nl.codingwithlinda.unit_conversion.data.weight.LBSWeight
import nl.codingwithlinda.unit_conversion.data.weight.convertWeight
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState
import kotlin.math.roundToInt

class WeightSettingViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val memento: UserSettingsMemento,
): ViewModel(){

    private val system = userSettingsRepo.unitSystemObservable

    private val _weightInputKg = MutableStateFlow(KGWeight(0.0))
    private val _weightInputPounds = MutableStateFlow(LBSWeight(0.0))


    val weightPounds = _weightInputPounds.map {
            WeightSettingUiState.Imperial(it)
    }
    val weightKg = _weightInputKg.map {
        WeightSettingUiState.SI(it)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val weightUiState = system.flatMapLatest{ system,  ->
        when (system) {
            is UnitSystems.SI -> weightKg
            is UnitSystems.IMPERIAL -> weightPounds
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeightSettingUiState.SI(KGWeight(0.0)))

    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {
                val weightGrams = GramsWeight(it.weightGrams)
                _weightInputKg.update {
                    convertWeight(weightGrams, KG) as KGWeight
                }
                _weightInputPounds.update {
                    convertWeight(weightGrams, LBS) as LBSWeight
                }
            }
        }
    }
    fun onAction(action: ActionWeightInput) {
        when (action) {
            is ActionWeightInput.KgInput -> {
                println("--- ActionWeightInput.KgInput --- kg: ${action.kg}")

                val kg = KGWeight(action.kg.toDouble())
                _weightInputKg.update {
                    kg
                }
                val previousPounds = fromPreviousPounds(kg,_weightInputPounds.value.weight.roundToInt())

                println("--- ActionWeightInput.KgInput --- previousPounds: $previousPounds")


                _weightInputPounds.update {
                   previousPounds.copy(
                      weight = previousPounds.weight.roundToInt().toDouble()
                   )
                }

            }

            is ActionWeightInput.PoundsInput -> {
                println("--- ActionWeightInput.PoundsInput --- pounds: ${action.pounds} ")

                val pounds = LBSWeight(action.pounds.toDouble())

                println("--- ActionWeightInput.PoundsInput --- LBSWeight: $pounds")

                val convertedToKg = convertWeight(pounds, KG) as KGWeight
                println("--- ActionWeightInput.PoundsInput --- convertedToKg: $convertedToKg")

                _weightInputKg.update {
                    convertedToKg
                }
                _weightInputPounds.update {
                    pounds
                }

            }

            is ActionWeightInput.Save -> {
                viewModelScope.launch(NonCancellable) {
                    val currentWeightKg = _weightInputKg.value
                    val currentWeightGrams = convertWeight(currentWeightKg, GRAM)
                    val userSettings = memento.restoreLast().copy(weightGrams = currentWeightGrams.weight)
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