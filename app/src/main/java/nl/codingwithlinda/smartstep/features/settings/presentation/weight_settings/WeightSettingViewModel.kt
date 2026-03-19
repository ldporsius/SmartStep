package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.fromPreviousPounds
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState
import nl.codingwithlinda.unit_conversion.data.weight.GramsWeight
import nl.codingwithlinda.unit_conversion.data.weight.KGWeight
import nl.codingwithlinda.unit_conversion.data.weight.LBSWeight
import nl.codingwithlinda.unit_conversion.data.weight.WeightUnitConverter
import nl.codingwithlinda.unit_conversion.domain.UnitSystems
import kotlin.math.roundToInt

class WeightSettingViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val memento: UserSettingsMemento,
    private val nonCancellableScope: CoroutineScope
): ViewModel(){

    private val system = userSettingsRepo.unitSystemObservable

    private val _weightInputKg = MutableStateFlow(KGWeight(0.0))
    private val _weightInputPounds = MutableStateFlow(LBSWeight(0.0))

    private val converter = WeightUnitConverter


    val weightPounds = _weightInputPounds.map {
            WeightSettingUiState.Imperial(it)
    }
    val weightKg = _weightInputKg.map {
        WeightSettingUiState.SI(it)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val weightUiState = system.flatMapLatest{ system,  ->
        when (system) {
            UnitSystems.SI -> weightKg
            UnitSystems.IMPERIAL -> weightPounds
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeightSettingUiState.SI(
        KGWeight(
            0.0
        )
    ))

    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {
                val weightGrams = GramsWeight(it.weightGrams)
                _weightInputKg.update {
                    converter.toKg(weightGrams)
                }
                _weightInputPounds.update {
                    converter.toLbs(weightGrams)
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

                val convertedToKg = converter.toKg(pounds)
                println("--- ActionWeightInput.PoundsInput --- convertedToKg: $convertedToKg")

                _weightInputKg.update {
                    convertedToKg
                }
                _weightInputPounds.update {
                    pounds
                }

            }

            is ActionWeightInput.Save -> {
                viewModelScope.launch{
                    val currentWeightKg = _weightInputKg.value
                    val currentWeightGrams = converter.toGram(currentWeightKg)
                    val userSettings = memento.restoreLast().copy(weightGrams = currentWeightGrams.weight)
                    memento.save(userSettings)
                }
            }
            is ActionWeightInput.ChangeSystem -> {
                nonCancellableScope.launch{
                    userSettingsRepo.saveUnitSystem(action.system)
                }
            }
        }
    }
}