package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionUnitInput
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.HeightUnitConverter

class UserSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val heightUnitConverter: HeightUnitConverter
): ViewModel() {

    private val _userSettings = MutableStateFlow<UserSettings>(UserSettings())
    val userSettings = _userSettings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UserSettings())

    private val _unitChoice = MutableStateFlow<UnitSystemUnits>(UnitSystemUnits.FEET_INCHES)
    val unitChoice = _unitChoice.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UnitSystemUnits.FEET_INCHES)

    fun onUnitChange(unit: UnitSystemUnits){
        _unitChoice.value = unit
    }

    fun handleHeightInput(actionUnitInput: ActionUnitInput){
        when(actionUnitInput){
            is ActionUnitInput.CmInput -> {

            }
            is ActionUnitInput.FeetInput ->{

            }
            is ActionUnitInput.InchesInput -> {

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