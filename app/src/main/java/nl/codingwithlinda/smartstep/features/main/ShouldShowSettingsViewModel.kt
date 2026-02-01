package nl.codingwithlinda.smartstep.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo

class ShouldShowSettingsViewModel(
    private val userSettingsRepo: UserSettingsRepo
) : ViewModel() {

    val isChecking = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            userSettingsRepo.loadSkip()
            isChecking.value = false
        }
    }
    val shouldShowSettings = userSettingsRepo.skippedObservable.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun skip(){
        viewModelScope.launch {
            userSettingsRepo.skip()
        }
    }
}