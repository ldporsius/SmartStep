package nl.codingwithlinda.smartstep.features.steps.edit.presentation

import androidx.lifecycle.viewmodel.compose.viewModel
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.features.steps.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditStepsViewModelTest {

    private lateinit var viewModel: EditStepsViewModel
    private val fakeDailyStepRepo = FakeDailyStepRepo()

    val testDispatcher = StandardTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EditStepsViewModel(fakeDailyStepRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `test editsteps viewmodel - steps are replaced on save`() = runTest(testDispatcher) {
        viewModel.steps.test {
            val item = awaitItem()
            assertEquals(0, item)

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

            println("first emission received")

            viewModel.onAction(EditStepAction.InputYear(2026))
            val emYear = awaitItem()

            println("year emission received")

            assertEquals(2026, emYear.YYYY)

            viewModel.onAction(EditStepAction.InputMonth(2))

            val emMonth = awaitItem()
            println("month emission received")

            assertEquals(2, emMonth.MM)

            viewModel.onAction(EditStepAction.InputDay(31))


            val emDay = awaitItem()
                assertEquals(31, emDay.DD)

               // assertThat(fakeDailyStepRepo.getStepCountForYYYYMMDD(date)?.stepCount).isEqualTo(1000)
            }
        }


}