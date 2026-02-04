package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionUnitInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.HeightUnitConverter

class UserSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val heightUnitConverter: HeightUnitConverter
): ViewModel() {

    val userSettingsState = userSettingsRepo.userSettingsObservable.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    private val _heightInput = MutableStateFlow(userSettingsState.value.height)

    private val _unitChoice = MutableStateFlow<UnitSystemUnits>(UnitSystemUnits.FEET_INCHES)
    val unitChoice = _unitChoice.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UnitSystemUnits.FEET_INCHES)

    private val _heightUiState = MutableStateFlow<HeightSettingUiState>(HeightSettingUiState.Imperial(feet = 5, inches = 7))
    val heightUiState = _heightUiState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HeightSettingUiState.Imperial(feet = 5, inches = 7))

    fun onUnitChange(unit: UnitSystemUnits){
        _unitChoice.update {
            unit
        }

        when(unit){
            UnitSystemUnits.CM -> {
                val currentHeightCm = _heightInput.value
                println("--- USERSETTINGSVIEWMODEL --- SI selected. currentHeightCm: $currentHeightCm")
                _heightUiState.update {
                    HeightSettingUiState.SI(cm = _heightInput.value)
                }
            }

            UnitSystemUnits.FEET_INCHES -> {
                val currentHeightCm = _heightInput.value
                println("--- USERSETTINGSVIEWMODEL --- Imperial selected. currentHeightCm: $currentHeightCm")
                val (feet, inches) = heightUnitConverter.toImperial(currentHeightCm.toDouble())
                println("--- USERSETTINGSVIEWMODEL --- converted ${currentHeightCm} to feet: $feet inches: $inches")
                _heightUiState.update {
                    HeightSettingUiState.Imperial(feet = feet.toInt(), inches = inches.toInt())
                }
            }
        }
    }


    fun handleHeightInput(actionUnitInput: ActionUnitInput){
        when(actionUnitInput){
            is ActionUnitInput.CmInput -> {
                println("--- USERSETTINGSVIEWMODEL --- cm input: ${actionUnitInput.cm}")

                _heightInput.update {
                    actionUnitInput.cm
                }
                _heightUiState.update {
                    HeightSettingUiState.SI(cm = actionUnitInput.cm)
                }
                println("--- USERSETTINGSVIEWMODEL --- value userSettings height after update: ${_heightInput.value}")
            }
            is ActionUnitInput.ImperialInput ->{
                println("--- USERSETTINGSVIEWMODEL --- imperial input: feet: ${actionUnitInput.feet} , inches:${actionUnitInput.inches}")
                val update = heightUnitConverter.fromUi(actionUnitInput.feet.toString(), actionUnitInput.inches.toString())
                _heightInput.update {
                    update.toInt()
                }
                _heightUiState.update {
                    HeightSettingUiState.Imperial(feet = actionUnitInput.feet, inches = actionUnitInput.inches)
                }
            }

            is ActionUnitInput.ActionSave -> {
                viewModelScope.launch(NonCancellable) {
                    val currentHeight = _heightInput.value
                    val userSettings = UserSettings(
                        height = currentHeight,
                    )
                    userSettingsRepo.saveSettings(userSettings)
                }
            }
        }
    }



    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {settings ->
                _heightInput.update {
                    settings.height
                }
            }
        }
    }
}