package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo

class UserSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo
): ViewModel() {

    private val _userSettings = MutableStateFlow<UserSettings>(UserSettings())
    val userSettings = _userSettings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UserSettings())

    fun skip(){
        viewModelScope.launch {
            userSettingsRepo.skip()
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