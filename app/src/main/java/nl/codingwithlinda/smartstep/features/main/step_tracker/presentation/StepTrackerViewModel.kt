package nl.codingwithlinda.smartstep.features.main.step_tracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState

class StepTrackerViewModel(
    private val stepTracker: StepTrackerImpl
): ViewModel() {

    val state = stepTracker.stateObservable
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StepTrackerState.STOPPED)

    private val _counter = MutableStateFlow(0)
    val counter = _counter.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val stepsTaken = stepTracker.stepsTaken
        .onEach {
            _counter.value += it
        }
        .launchIn(viewModelScope)

    init {
        stepsTaken.start()
    }
    fun pause(){
        stepTracker.pause()
    }

    fun start(){
        stepTracker.start()
    }
}