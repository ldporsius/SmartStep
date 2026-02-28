package nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.tests.di.TestDispatcherProvider
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class EditStepsViewModelTest {

    private lateinit var viewModel: EditStepsViewModel
    private val fakeDailyStepRepo = FakeDailyStepRepo()
    private val todaysStep = DailyStepCountCreator.create(100)
    val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EditStepsViewModel(
            fakeDailyStepRepo,
            appScope = CoroutineScope(testDispatcher),
        )

        runBlocking {
            fakeDailyStepRepo.saveStepCount(todaysStep)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        fakeDailyStepRepo.reset()
    }


    @Test
    fun `test editsteps viewmodel - steps are replaced on save`() = runTest(testDispatcher) {
        viewModel.steps.test {

            val item0 = awaitItem()
            assertEquals(100, item0)

            println("first emission received")

            viewModel.onAction(EditStepAction.SetSteps("1000"))

            val item2 = awaitItem()
            assertEquals(1000, item2)


            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `test editStepViewModel - date is correct`() = runTest(testDispatcher) {
        viewModel.dateYYYYMMDD.test {
            val item = awaitItem()

            println("first emission received $item")

            val tomorrowLocal = LocalDate.ofEpochDay(todaysStep.dayEpochDay).plusDays(10)

            viewModel.onAction(EditStepAction.InputYear(tomorrowLocal.year))
            val emYear = awaitItem()

            println("year emission received")

            assertEquals(tomorrowLocal.year, emYear.YYYY)

            viewModel.onAction(EditStepAction.InputMonth(tomorrowLocal.monthValue))

            val emMonth = awaitItem()
            println("month emission received: ${emMonth.MM} ${emMonth.DD}")

            assertEquals(tomorrowLocal.monthValue, emMonth.MM)

            viewModel.onAction(EditStepAction.InputDay(tomorrowLocal.dayOfMonth))

            val emDay = awaitItem()
            println("day emission received")

            assertEquals(tomorrowLocal.dayOfMonth, emDay.DD)
            }
        }


}