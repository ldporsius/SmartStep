package nl.codingwithlinda.smartstep.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo

class ShouldShowSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo
) : ViewModel() {

    val isChecking = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            userSettingsRepo.loadIsOnboarding()
            isChecking.value = false
        }
    }
    val shouldShowSettings = userSettingsRepo.isOnboardingObservable
        .map {
            it.not()
        }
        .stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(),
        null
    )

    fun skip(){
        viewModelScope.launch {
            userSettingsRepo.setIsOnboardingFalse()
        }
    }
}