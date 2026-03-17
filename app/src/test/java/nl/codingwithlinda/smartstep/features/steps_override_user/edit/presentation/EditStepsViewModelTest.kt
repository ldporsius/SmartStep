package nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.EditStepsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.util.BaseStepRepoTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class EditStepsViewModelTest: BaseStepRepoTest() {

    private lateinit var viewModel: EditStepsViewModel

    private val todaysStep = DailyStepCountCreator.create(100)


    @Before
    override fun setup() {
        super.setup()
        viewModel = EditStepsViewModel(
            fakeDailyStepRepo,
            appScope = CoroutineScope(testDispatcher),
        )
    }

    @Test
    fun `test editsteps viewmodel - steps are replaced on save`() = runTest(testDispatcher) {

        activityRecognitionRepo.saveStepCount(todaysStep)

        viewModel.steps.test {

            val item0 = awaitItem()
            println("item0")
            assertEquals(100, item0)

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