package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal

import android.util.Log.i
import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.tests.FakeStepTracker
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DailyStepGoalViewModelTest {

    val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: DailyStepGoalViewModel
    lateinit var fakeStepTracker: FakeStepTracker

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeStepTracker = FakeStepTracker(CoroutineScope(testDispatcher))
        viewModel = DailyStepGoalViewModel(
            appScope = CoroutineScope(testDispatcher),
            dailyStepRepo = FakeDailyStepRepo(),
            stepTracker = fakeStepTracker

        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

    }

    @Test
    fun `test dailystepviewmodel - step count updated`()= runTest{

        viewModel.stepCount.test {

            repeat(10){i->
                val em1 = awaitItem()
                assertEquals(i, em1)
                println("$em1")

            }

            fakeStepTracker.stop()
            cancelAndIgnoreRemainingEvents()
        }
    }

}