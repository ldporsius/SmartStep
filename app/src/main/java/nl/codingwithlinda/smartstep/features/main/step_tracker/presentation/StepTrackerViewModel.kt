package nl.codingwithlinda.smartstep.features.main.step_tracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.di.DispatcherProvider
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationEnd
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo

class StepTrackerViewModel(
    private val stepTracker: StepTracker,
    private val walkDurationRepo: WalkDurationRepo,
    dispatcherProvider: DispatcherProvider
): ViewModel() {

    val state = stepTracker.stateObservable
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StepTrackerState.STOPPED)

    private val _counter = MutableStateFlow(0)
    val counter = _counter.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val stepsTaken = stepTracker.stepsTaken
        .onEach {
            _counter.value = it.stepCount
        }
        .launchIn(viewModelScope)

    init {
        stepsTaken.start()

        viewModelScope.launch(dispatcherProvider.io) {
            state.collect {state ->
                println("state changed: $state")
                when(state){
                    StepTrackerState.STARTED -> {
                        val today = DailyStepCountCreator.toDateYYYYMMDD(System.currentTimeMillis())
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
                        val today = DailyStepCountCreator.toDateYYYYMMDD(System.currentTimeMillis())
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
                        val today = DailyStepCountCreator.toDateYYYYMMDD(System.currentTimeMillis())
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