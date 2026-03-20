package nl.codingwithlinda.smartstep.features.main.presentation.walk_duration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.core.domain.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.step_tracker.StepTrackerState

class WalkDurationViewModel(
    private val stepTracker: StepTracker,
): ViewModel() {

    val state = stepTracker.stateObservable
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StepTrackerState.STOPPED)


    fun pause(){
        stepTracker.pause()
    }

    fun start(){
        stepTracker.start()
    }
}