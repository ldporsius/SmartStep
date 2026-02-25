package nl.codingwithlinda.smartstep.features.steps_override_user.reset.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.util.BaseJunitTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ResetStepsViewModelTest: BaseJunitTest() {

    private lateinit var viewModel: ResetStepsViewModel
    private val fakeDailyStepRepo  = FakeDailyStepRepo()

    private val scope = CoroutineScope(testDispatcher)

    private val step = DailyStepCountCreator.create(100)
    @Before
    fun setup1(){
        viewModel = ResetStepsViewModel(
            fakeDailyStepRepo,
            step,
            scope
        )
    }
    @After
    fun teardown1(){
        fakeDailyStepRepo.reset()
    }


    @Test
    fun `reset   success   today s step count exists`() : Unit = runTest (testDispatcher){
        // Verify that when a step count for today exists, it is fetched and its value is saved as the new baseline.

        fakeDailyStepRepo.saveStepCount(step)
        val userOverride = DailyStepCountCreator.create(200)
        fakeDailyStepRepo.saveDailyStepCountUserOverride(userOverride.dateYYYYMMDD, userOverride.stepCount)

        viewModel.reset()

        val today = DateTimeHelper.toDateYYYYMMDD(System.currentTimeMillis())

        val result = fakeDailyStepRepo.getStepCountForDate(today.dateEpochDay)
        assertThat(result!!.stepCount).isZero()

        val todaysSteps = fakeDailyStepRepo.getStepCountForDate(System.currentTimeMillis())?.stepCount ?: 0
        fakeDailyStepRepo.stepCountPlusUserOverride.test {
            val override = awaitItem()
            assertThat(override).isEqualTo(200)
        }

    }


    @Test
    fun `reset   success   today s step count does not exist`(): Unit = runTest(testDispatcher){
        // Verify that when no step count for today exists, the baseline is not updated, and a new step count of 0 is created and saved.
        viewModel.reset()
        //runCurrent()
        val today = DateTimeHelper.toDateYYYYMMDD(System.currentTimeMillis())
        println("today: ${today.dateString}")

        val result = fakeDailyStepRepo.getStepCountForDate(today.dateEpochDay)
        assertThat(result!!.stepCount).isZero()

    }

    @Ignore
    @Test
    fun `reset     new step count creation and save`() {
        // Verify that after the baseline logic, a new DailyStepCount object with 0 steps for the current day is created and saved to the repository.
        // TODO implement test
    }

    @Ignore
    @Test
    fun `reset     coroutine launch verification`() {
        // Confirm that the logic inside the reset function is executed within the coroutine scope provided by SmartStepApplication.applicationScope.
        // TODO implement test
    }

    @Ignore
    @Test
    fun `reset     exception in getStepCountForDate`() {
        // Test the behavior when dailyStepRepo.getStepCountForDate(it) throws an exception.
        // The function should handle it gracefully and still proceed to create a new step count of 0.
        // TODO implement test
    }

    @Ignore
    @Test
    fun `reset     exception in saveDailyStepCountBaseline`() {
        // Test the behavior when dailyStepRepo.saveDailyStepCountBaseline(it) throws an exception.
        // The function should handle the error and proceed to create and save the new step count of 0.
        // TODO implement test
    }

    @Ignore
    @Test
    fun `reset     exception in saveStepCount`() {
        // Test the behavior when the final dailyStepRepo.saveStepCount(it) call throws an exception and verify the system handles this failure.
        // TODO implement test
    }

    @Ignore
    @Test
    fun `reset     threading and concurrency`() {
        // Test for race conditions by calling reset() multiple times in quick succession from different threads to ensure data integrity and final state consistency.
        // TODO implement test
    }
}