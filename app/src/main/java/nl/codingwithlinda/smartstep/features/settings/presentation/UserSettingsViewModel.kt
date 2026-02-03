package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.userSettingsRepo
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

    private val _userSettings = MutableStateFlow<UserSettings>(UserSettings())
    val userSettings = _userSettings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UserSettings())

    private val _unitChoice = MutableStateFlow<UnitSystemUnits>(UnitSystemUnits.FEET_INCHES)
    val unitChoice = _unitChoice.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UnitSystemUnits.FEET_INCHES)

    private val _heightUiState = MutableStateFlow<HeightSettingUiState>(HeightSettingUiState.Imperial(feet = 5, inches = 9))
    val heightUiState = _heightUiState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HeightSettingUiState.Imperial(feet = 5, inches = 9))

    fun onUnitChange(unit: UnitSystemUnits){
        _unitChoice.value = unit

        when(unit){
            UnitSystemUnits.CM -> {
                _heightUiState.update {
                    HeightSettingUiState.SI(cm = userSettings.value.height)
                }
            }

            UnitSystemUnits.FEET_INCHES -> {
                val (feet, inches) = heightUnitConverter.toUi(userSettings.value.height.toDouble())
                _heightUiState.update {
                    HeightSettingUiState.Imperial(feet = feet.toInt(), inches = inches.toInt())
                }
            }
        }
    }


    fun handleHeightInput(actionUnitInput: ActionUnitInput){
        when(actionUnitInput){
            is ActionUnitInput.CmInput -> {
                _userSettings.update {
                    it.copy(height = actionUnitInput.cm)
                }
                _heightUiState.update {
                    HeightSettingUiState.SI(cm = actionUnitInput.cm)
                }
            }
            is ActionUnitInput.ImperialInput ->{
                val update = heightUnitConverter.fromUi(actionUnitInput.feet.toString(), actionUnitInput.inches.toString())
                _userSettings.update {
                    it.copy(height = update.toInt())
                }
                _heightUiState.update {
                    HeightSettingUiState.Imperial(feet = actionUnitInput.feet, inches = actionUnitInput.inches)
                }
            }

        }
    }



    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {
                _userSettings.value = it
            }
        }
    }
}