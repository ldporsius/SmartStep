package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.unit_conversion.data.lenght.FeetInchesUnitConverter
import nl.codingwithlinda.unit_conversion.data.lenght.LengthUnitConverter.Cm
import nl.codingwithlinda.unit_conversion.data.lenght.lenght_defs.FeetInches
import nl.codingwithlinda.unit_conversion.domain.UnitSystems
import kotlin.math.roundToInt

class HeightSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val memento: UserSettingsMemento,
    private val nonCancellableScope: CoroutineScope
): ViewModel() {

    private val feetInchesConverter = FeetInchesUnitConverter()
    private val _heightInput = MutableStateFlow(Cm(0.0))

    private val unitSystemPrefs = userSettingsRepo.unitSystemObservable

    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {settings ->
                println("--- LOADED SETTINGS FROM REPO: $settings")
                _heightInput.update {
                    Cm(settings.heightCm.toDouble())
                }
            }
        }
    }
    val heightUiState = unitSystemPrefs.combine(_heightInput){ system, input ->
        when(system){
            UnitSystems.SI -> HeightSettingUiState.SI(cm = input)
            UnitSystems.IMPERIAL -> {
                val feetInches = feetInchesConverter.convertToFeetInches(input)
                HeightSettingUiState.Imperial(feetInches)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        HeightSettingUiState.SI(Cm(0.0)))

    fun handleHeightInput(actionUnitInput: ActionHeightInput){
        when(actionUnitInput) {
            is ActionHeightInput.CmInput -> {
                _heightInput.update {
                    Cm(actionUnitInput.cm.toDouble())
                }
            }

            is ActionHeightInput.ImperialInput -> {
                val feetInches = FeetInches(actionUnitInput.feet, actionUnitInput.inches)
                val update = feetInchesConverter.toCm(feetInches)
                _heightInput.update {
                    update
                }
            }

            is ActionHeightInput.ActionSave -> {
                val currentHeight = _heightInput.value.value.roundToInt()
                val userSettings = memento.restoreLast().copy(heightCm = currentHeight)
                memento.save(userSettings)
            }

            is ActionHeightInput.ChangeUnitSystem -> {
                nonCancellableScope.launch{
                    userSettingsRepo.saveUnitSystem(actionUnitInput.system)
                }
            }
        }
    }
}