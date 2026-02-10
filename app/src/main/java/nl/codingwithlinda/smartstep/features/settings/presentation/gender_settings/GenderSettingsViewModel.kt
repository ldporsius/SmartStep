package nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.settings.Gender
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings.state.ActionGender

class GenderSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val memento: UserSettingsMemento
): ViewModel() {

    private val _genderUi = MutableStateFlow(Gender.FEMALE)
    val genderUi = _genderUi.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _genderUi.value)
    fun onAction(action: ActionGender){
        when(action){
            is ActionGender.ChangeGender -> {
                _genderUi.update {
                    action.gender
                }
                viewModelScope.launch {
                    memento.restoreLast().copy(gender = action.gender).also {
                        memento.save(it)
                    }
                }
            }
        }
    }
}