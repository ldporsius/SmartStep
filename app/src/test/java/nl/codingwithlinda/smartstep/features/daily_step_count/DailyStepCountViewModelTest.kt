package nl.codingwithlinda.smartstep.features.daily_step_count

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.tests.FakeActivityRecognitionRepo
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.tests.FakeStepTracker
import nl.codingwithlinda.smartstep.util.BaseJunitTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DailyStepCountViewModelTest: BaseJunitTest() {

    lateinit var viewModel: DailyStepCountViewModel
    lateinit var fakeStepTracker: FakeStepTracker


    @Before
    override fun setup() {
        super.setup()

        fakeStepTracker = FakeStepTracker(CoroutineScope(testDispatcher))

        viewModel = DailyStepCountViewModel(
            dailyStepRepo = fakeDailyStepRepo,
            )

    }


    @Test
    fun `test dailystepviewmodel - step count updated`()= runTest(testDispatcher) {
        fakeStepTracker.start()

        backgroundScope.launch {
            fakeStepTracker.stepsTaken.collect {
                println("--- test step taken : $it")
                activityRecognitionRepo.saveStepCount(
                    it
                )
            }
        }

            viewModel.stepsToday.test {

                val em0 = awaitItem()

                val em1 = awaitItem()
                println("$em1")

                val em2 = awaitItem()

                println("$em2")
                assertThat(em2).isEqualTo(2000)
                fakeStepTracker.stop()

                cancelAndIgnoreRemainingEvents()
            }

    }

}