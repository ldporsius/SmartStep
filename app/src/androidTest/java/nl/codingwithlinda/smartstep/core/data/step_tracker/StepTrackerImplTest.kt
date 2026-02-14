package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.R.attr.action
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.EventCondition
import androidx.test.uiautomator.UiDevice
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.MainActivity
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import org.junit.Assert.*
import org.junit.Test

class StepTrackerImplTest {

    val context = ApplicationProvider.getApplicationContext<SmartStepApplication>()
    val scope = SmartStepApplication.applicationScope
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    @Test
    fun testStepTrackerImpl(){
        val stepTracker = StepTrackerImpl.getInstance(
        context, scope)
        stepTracker.initialize()

        scope.launch {
            stepTracker.stepsTaken.collect {
                println("Another step taken: $it")
            }
        }


    }

}