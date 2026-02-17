package nl.codingwithlinda.smartstep.features.main.step_tracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState

class StepTrackerViewModel(
    private val stepTracker: StepTrackerImpl
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