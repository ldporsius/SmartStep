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

    private val userSettingsInitial = UserSettings()

    private val _userSettings = MutableStateFlow<UserSettings>(userSettingsInitial)
    val userSettings = _userSettings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), userSettingsInitial)

    private val _heightInput = MutableStateFlow(userSettingsInitial.height)
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
                val (feet, inches) = heightUnitConverter.toUi(currentHeightCm.toDouble())
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
                val currentWeight = userSettings.value.weight
                val currentGender = userSettings.value.gender

                _heightInput.update {
                    actionUnitInput.cm
                }
                _userSettings.update {
                    UserSettings(
                        gender = currentGender,
                        weight = currentWeight,
                        height = actionUnitInput.cm)
                }
                _heightUiState.update {
                    HeightSettingUiState.SI(cm = actionUnitInput.cm)
                }
                println("--- USERSETTINGSVIEWMODEL --- value userSettings height after update: ${userSettings.value.height}")
            }
            is ActionUnitInput.ImperialInput ->{
                println("--- USERSETTINGSVIEWMODEL --- imperial input: feet: ${actionUnitInput.feet} , inches:${actionUnitInput.inches}")
                val update = heightUnitConverter.fromUi(actionUnitInput.feet.toString(), actionUnitInput.inches.toString())
                _heightInput.update {
                    update.toInt()
                }
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