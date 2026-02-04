package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.HeightUnitConverter

class HeightSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val heightUnitConverter: HeightUnitConverter
): ViewModel() {

    val userSettingsState = userSettingsRepo.userSettingsObservable.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings()
    )

    private val _heightInput = MutableStateFlow(0)

    private val unitSystemPrefs = userSettingsRepo.unitSystemObservable.onEach {
        println("--- USERSETTINGSVIEWMODEL --- unitSystemPrefs: $it")

    }

    val heightUiState = unitSystemPrefs.combine(_heightInput){ system, input ->

        when(system){
            is UnitSystemUnits.SI -> HeightSettingUiState.SI(valueCm = input)
            is UnitSystemUnits.IMPERIAL -> HeightSettingUiState.Imperial(valueCm = input)
        }.also {
            println("--- USERSETTINGSVIEWMODEL --- heightUiState changes in combine flow: $it")
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        HeightSettingUiState.SI(0))




    fun handleHeightInput(actionUnitInput: ActionHeightInput){
        when(actionUnitInput) {
            is ActionHeightInput.CmInput -> {
                println("--- USERSETTINGSVIEWMODEL --- cm input: ${actionUnitInput.cm}")

                _heightInput.update {
                    actionUnitInput.cm
                }

                println("--- USERSETTINGSVIEWMODEL --- value userSettings height after update: ${_heightInput.value}")
            }

            is ActionHeightInput.ImperialInput -> {
                println("--- USERSETTINGSVIEWMODEL --- imperial input: feet: ${actionUnitInput.feet} , inches:${actionUnitInput.inches}")

                val update = heightUnitConverter.toSI(actionUnitInput.feet, actionUnitInput.inches)
                _heightInput.update {
                    update.toInt()
                }
            }

            is ActionHeightInput.ActionSave -> {
                viewModelScope.launch(NonCancellable) {
                    val currentHeight = _heightInput.value
                    val userSettings = UserSettings(
                        height = currentHeight,
                    )
                    userSettingsRepo.saveSettings(userSettings)
                }
            }

            is ActionHeightInput.ChangeUnitSystem -> {
                viewModelScope.launch(NonCancellable) {
                    userSettingsRepo.saveUnitSystem(actionUnitInput.system)
                }
            }
        }
    }



    init {
        viewModelScope.launch {
            userSettingsRepo.loadSettings().also {settings ->
                println("--- LOADED SETTINGS FROM REPO: $settings")
                _heightInput.update {
                    settings.height
                }
            }
        }
    }
}