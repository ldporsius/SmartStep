package nl.codingwithlinda.smartstep.core.data.step_tracker

import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTrackerState
import org.junit.Test

class StepTrackerImplTest {

    val context = ApplicationProvider.getApplicationContext<SmartStepApplication>()
    val scope = SmartStepApplication.applicationScope
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    val callback = {state: StepTrackerState ->
        println("StepTracker state changed: $state")
    }
    @Test
    fun testStepTrackerImpl(){
        val stepTracker = StepTrackerDetectorImpl.getInstance(
        context, scope)


        scope.launch {
            stepTracker.stepsTaken.collect {
                println("Another step taken: $it")
            }
        }

        scope.launch {
            stepTracker.stateObservable.collect {
                callback(it)
            }
        }
        runBlocking {
            stepTracker.start()
            delay(100)
            stepTracker.pause()
            delay(100)
            stepTracker.stop()
        }

    }

}