package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState

class FakeStepTracker(
    private val scope: CoroutineScope
): StepTracker {

    private val _stepsTaken = Channel<Int>()
    private val state = MutableStateFlow(StepTrackerState.STOPPED)

    var isCounting = false

    override fun pause() {
        isCounting = false
    }

    override fun start() {
        isCounting = true
        scope.launch {
            while (isCounting) {
                _stepsTaken.send(1)
                delay(1000)
            }
        }
    }

    override fun stop() {
        isCounting = false
    }

    override val stepsTaken: Flow<DailyStepCount> = _stepsTaken.receiveAsFlow()
        .map {
            DailyStepCountCreator.create(
                count = it
            )
        }

    override val stateObservable: Flow<StepTrackerState>
        get() = state


}