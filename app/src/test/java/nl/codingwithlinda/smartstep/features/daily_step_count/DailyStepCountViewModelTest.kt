package nl.codingwithlinda.smartstep.features.daily_step_count

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.FakeStepTracker
import nl.codingwithlinda.smartstep.util.BaseStepRepoTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DailyStepCountViewModelTest: BaseStepRepoTest(
    testDispatcher = UnconfinedTestDispatcher()
) {

    lateinit var viewModel: DailyStepCountViewModel
    lateinit var fakeStepTracker: FakeStepTracker


    @Before
    override fun setup() {
        super.setup()

        fakeStepTracker = FakeStepTracker(testDispatcher)

        viewModel = DailyStepCountViewModel(
            dailyStepRepo = fakeDailyStepRepo,
        )

    }

    @Test
    fun `test dailystepviewmodel - step count updated`(): Unit = runTest(testDispatcher) {
        fakeStepTracker.start()

        val job = backgroundScope.launch {
            fakeStepTracker.stepsTaken
                .take(11)
                .collect { count ->
                    activityRecognitionRepo.saveStepCount(count)
                }
        }

        job.join()
        job.cancel()
        println("counting done")


        fakeDailyStepRepo.stepCountPlusUserOverride
            .stateIn(CoroutineScope(testDispatcher))
            .value.let {
                println("repo step count: $it")
            }

        val result = mutableListOf<Int>()
        val job2 = launch {
            viewModel.stepsToday
                .take(2)
                .collect {
                result.add(it)
            }

        }


        runCurrent()
        advanceUntilIdle()

        job2.join()


        println("count: $result")
        assert(result.last() == 10)


    }

}