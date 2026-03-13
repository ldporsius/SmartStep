package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

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
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.unit_conversion.data.lenght.Cm
import nl.codingwithlinda.unit_conversion.data.lenght.FeetInches
import nl.codingwithlinda.unit_conversion.domain.UnitSystems
import kotlin.math.roundToInt

class HeightSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val memento: UserSettingsMemento,
): ViewModel() {

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
            is UnitSystems.SI -> HeightSettingUiState.SI(cm = input)
            is UnitSystems.IMPERIAL -> {
                val feetInches = input.convert()
                HeightSettingUiState.Imperial(feetInches)
            }
        }.also {
            //println("--- USERSETTINGSVIEWMODEL --- heightUiState changes in combine flow: $it")
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        HeightSettingUiState.SI(Cm(0.0)))

    fun handleHeightInput(actionUnitInput: ActionHeightInput){
        when(actionUnitInput) {
            is ActionHeightInput.CmInput -> {
                println("--- USERSETTINGSVIEWMODEL --- cm input: ${actionUnitInput.cm}")

                _heightInput.update {
                    Cm(actionUnitInput.cm.toDouble())
                }.also {
                    println("--- USERSETTINGSVIEWMODEL --- value userSettings height after update: ${_heightInput.value}")
                }

            }

            is ActionHeightInput.ImperialInput -> {
                println("--- USERSETTINGSVIEWMODEL --- imperial input: feet: ${actionUnitInput.feet} , inches:${actionUnitInput.inches}")

                val feetInches = FeetInches(actionUnitInput.feet, actionUnitInput.inches)
                val update = feetInches.valueCm
                _heightInput.update {
                    Cm(update)
                }
            }

            is ActionHeightInput.ActionSave -> {
                viewModelScope.launch(NonCancellable) {
                    val currentHeight = _heightInput.value.valueCm.roundToInt()
                    val userSettings = memento.restoreLast().copy(heightCm = currentHeight)
                    memento.save(userSettings)
                }
            }

            is ActionHeightInput.ChangeUnitSystem -> {
                viewModelScope.launch(NonCancellable) {
                    userSettingsRepo.saveUnitSystem(actionUnitInput.system)
                }
            }
        }
    }
}