package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal

import android.util.Log.i
import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
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
    lateinit var repo : FakeDailyStepRepo


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeStepTracker = FakeStepTracker(CoroutineScope(testDispatcher))
        repo = FakeDailyStepRepo()
        viewModel = DailyStepGoalViewModel(
            appScope = CoroutineScope(testDispatcher),
            dailyStepRepo = repo,

        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test dailystepviewmodel - step count updated`()= runTest{

        backgroundScope.launch {
            fakeStepTracker.stepsTaken.collect {
                println("--- test step taken : $it")
                repo.saveStepCount(
                    DailyStepCount(1, it)
                )
            }
        }
        viewModel.stepCount.test {
            fakeStepTracker.start()

                val em0 = awaitItem()
                assertEquals(em0, 0)


            val em1 = awaitItem()
            assertEquals(em1, 1)
            println("$em1")




            fakeStepTracker.stop()
            cancelAndIgnoreRemainingEvents()
        }
    }

}