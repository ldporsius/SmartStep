package nl.codingwithlinda.smartstep.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo

class ShouldShowSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo
) : ViewModel() {

    private val _isChecking = Channel<Boolean>()
    val isChecking = _isChecking.receiveAsFlow()


    init {
        viewModelScope.launch {
            userSettingsRepo.loadIsOnboarding()
            _isChecking.send(false)
        }
    }
    val shouldShowSettings = userSettingsRepo.isOnboardingObservable
        .map {
            it.not()
        }
        .stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        null
    )

    fun skip(){
        viewModelScope.launch {
            userSettingsRepo.setIsOnboardingFalse()
        }
    }
}