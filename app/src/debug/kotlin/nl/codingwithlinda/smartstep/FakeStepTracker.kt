package nl.codingwithlinda.smartstep

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.step_tracker.StepTrackerState
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator

class FakeStepTracker(
    private val dispatcher: CoroutineDispatcher
): StepTracker {

    private val _stepsTaken = MutableStateFlow(0)
    private val state = MutableStateFlow(StepTrackerState.STOPPED)

    override fun pause() {
        state.update {
            StepTrackerState.PAUSED
        }
    }


    private fun counter() = flow{
        var i = 0
        while (true) {
            delay(1000)
            i ++
            emit(i)
        }
    }.onEach {step ->
        println("--- FAKE STEP TRACKER IS COUNTING $step")
        _stepsTaken.update {
            step
        }
    }
    override fun start() {
        println("--- FAKE STEP TRACKER STARTED")
        state.update {
            StepTrackerState.STARTED
        }

        CoroutineScope(dispatcher).launch {
            counter().take(10).toList()
        }
    }

    override fun stop() {
        state.update {
            StepTrackerState.STOPPED
        }

    }

    val stepsTaken: Flow<DailyStepCount> = _stepsTaken
        .map {
            DailyStepCountCreator.create(
                count = it
            )
        }

    override val stateObservable: Flow<StepTrackerState> = state


}