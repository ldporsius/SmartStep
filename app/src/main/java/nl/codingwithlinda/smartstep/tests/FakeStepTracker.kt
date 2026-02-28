package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState

class FakeStepTracker(
    private val scope: CoroutineScope
): StepTracker {

    private val _stepsTaken = MutableStateFlow(0)
    private val state = MutableStateFlow(StepTrackerState.STOPPED)

    var isCounting = false

    override fun pause() {
        isCounting = false
    }

    private suspend fun bump(i:Int): Int{
        delay(1000)

        return i + 1
    }
    override fun start() {
        isCounting = true
        scope.launch {
            var count = 0
            while (count < 10) {
                count = bump(count)
            }
        }
    }

    override fun stop() {
        isCounting = false
    }

    override val stepsTaken: Flow<DailyStepCount> = _stepsTaken
        .map {
            DailyStepCountCreator.create(
                count = it
            )
        }

    override val stateObservable: Flow<StepTrackerState>
        get() = state


}