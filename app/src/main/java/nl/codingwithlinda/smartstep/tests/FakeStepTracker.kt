package nl.codingwithlinda.smartstep.tests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker

class FakeStepTracker(
    private val scope: CoroutineScope
): StepTracker {

    private val _stepsTaken = Channel<Int>()

    var isCounting = false

    override fun initialize() {
        isCounting = true
        scope.launch {
            while (isCounting) {
                _stepsTaken.send(1)
                delay(1000)
            }
        }
    }

    override fun start() {

    }

    override fun stop() {
        isCounting = false
    }

    override val stepsTaken: Flow<Int> = _stepsTaken.receiveAsFlow()


}