package nl.codingwithlinda.smartstep.features.main.presentation.walk_duration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationEnd
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper

class WalkDurationViewModel(
    private val stepTracker: StepTracker,
    private val walkDurationRepo: WalkDurationRepo,
    dispatcherProvider: DispatcherProvider
): ViewModel() {

    val state = stepTracker.stateObservable
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StepTrackerState.STOPPED)


    init {
        viewModelScope.launch(dispatcherProvider.io) {
            state.collect {state ->
                when(state){
                    StepTrackerState.STARTED -> {
                        walkDurationRepo.saveWalkDurationStart(
                            WalkDurationStart(
                                System.currentTimeMillis()
                            )
                        )
                    }
                    StepTrackerState.PAUSED -> {
                        walkDurationRepo.saveWalkDurationEnd(
                            WalkDurationEnd(
                                System.currentTimeMillis()
                            )
                        )
                    }
                    StepTrackerState.STOPPED -> {
                        walkDurationRepo.saveWalkDurationEnd(
                            WalkDurationEnd(
                                System.currentTimeMillis()
                            )
                        )
                    }
                }
            }

        }
    }
    fun pause(){
        stepTracker.pause()
    }

    fun start(){
        stepTracker.start()
    }
}