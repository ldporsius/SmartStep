package nl.codingwithlinda.smartstep.features.steps.edit.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.features.steps.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EditStepsViewModelTest {

    private lateinit var viewModel: EditStepsViewModel
    private val fakeDailyStepRepo = FakeDailyStepRepo()


    @Before
    fun setup() {
        viewModel = EditStepsViewModel(fakeDailyStepRepo)
    }

    @Test
    fun `test editsteps viewmodel`() = runTest {
        viewModel.steps.test {
            val item = awaitItem()
            assertEquals(0, item)

            viewModel.onAction(EditStepAction.SetSteps("1000"))

            val item2 = awaitItem()
            assertEquals(1000, item2)

            viewModel.onAction(EditStepAction.InputYear(2026))
            viewModel.onAction(EditStepAction.InputMonth(1))
            viewModel.onAction(EditStepAction.InputDay(31))


            viewModel.onAction(EditStepAction.Save)

            backgroundScope.launch {
                viewModel.dateYYYYMMDD.collect(){
                    val date = viewModel.dateYYYYMMDD.value
                    assertEquals(2026, date.YYYY)
                    assertEquals(1, date.MM)
                    assertEquals(31, date.DD)
                    assertThat(fakeDailyStepRepo.getStepCountForYYYYMMDD(date)?.stepCount).isEqualTo(1000)
                }
            }

            cancelAndConsumeRemainingEvents()

        }
    }
}