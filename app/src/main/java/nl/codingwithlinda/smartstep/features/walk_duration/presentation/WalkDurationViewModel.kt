package nl.codingwithlinda.smartstep.features.walk_duration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.di.DispatcherProvider
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

    private val today : DateYYYYMMDD
        get() = DateTimeHelper.toDateYYYYMMDD(System.currentTimeMillis())


    init {
        viewModelScope.launch(dispatcherProvider.io) {
            state.collect {state ->
                println("state changed: $state")
                when(state){
                    StepTrackerState.STARTED -> {
                        walkDurationRepo.saveWalkDurationStart(
                            WalkDurationStart(
                                today.YYYY,
                                today.MM,
                                today.DD,
                                System.currentTimeMillis()
                            )
                        )
                    }
                    StepTrackerState.PAUSED -> {
                        walkDurationRepo.saveWalkDurationEnd(
                            WalkDurationEnd(
                                today.YYYY,
                                today.MM,
                                today.DD,
                                System.currentTimeMillis()
                            )
                        )
                    }
                    StepTrackerState.STOPPED -> {
                        walkDurationRepo.saveWalkDurationEnd(
                            WalkDurationEnd(
                                today.YYYY,
                                today.MM,
                                today.DD,
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