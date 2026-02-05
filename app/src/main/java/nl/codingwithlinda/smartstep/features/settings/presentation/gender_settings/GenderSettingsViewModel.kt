package nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings.state.ActionGender

class GenderSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo
): ViewModel() {

    fun onAction(action: ActionGender){
        when(action){
            is ActionGender.ChangeGender -> {
                viewModelScope.launch {
                    userSettingsRepo.loadSettings().also {
                        userSettingsRepo.saveSettings(it.copy(gender = action.gender))
                    }
                }
            }
        }
    }
}