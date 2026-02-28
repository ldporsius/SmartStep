package nl.codingwithlinda.smartstep.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.tests.FakeActivityRecognitionRepo
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseJunitTest(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) {

    val activityRecognitionRepo = FakeActivityRecognitionRepo()
    val fakeDailyStepRepo = FakeDailyStepRepo(){
        activityRecognitionRepo.getStepCountForDate(it)
    }

    @Before
    open fun setup(){
        Dispatchers.setMain(testDispatcher)
    }

    @After
    open fun tearDown(){
        Dispatchers.resetMain()
    }
}