package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isCloseTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.WeightUnitConverter
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState
import nl.codingwithlinda.smartstep.tests.FakeUserSettingsRepo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeightSettingViewModelTest {
    val testDispatcher = StandardTestDispatcher()

    lateinit var viewModel: WeightSettingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeightSettingViewModel(
            userSettingsRepo = FakeUserSettingsRepo(),
            memento = UserSettingsMemento,
            weightUnitConverter = WeightUnitConverter
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test weightViewModel updates correctly`() = runTest {
        viewModel.weightUiState.test {
            assertEquals(awaitItem(), WeightSettingUiState.SI(0))

            viewModel.onAction(ActionWeightInput.KgInput(100))

            assertEquals(awaitItem(), WeightSettingUiState.SI(100))

            viewModel.onAction(ActionWeightInput.ChangeSystem(UnitSystemUnits.IMPERIAL))


            val item1 = awaitItem()
            assertTrue(item1 is WeightSettingUiState.Imperial)
            assertEquals(item1, WeightSettingUiState.Imperial(100.0))
            with(item1 as WeightSettingUiState.Imperial) {
                assertEquals(pounds, 220)
            }
        }
    }

    @Test
    fun `test weightViewModel updates pounds correctly`() = runTest {
        viewModel.weightUiState.test {
            assertEquals(awaitItem(), WeightSettingUiState.SI(0))
            viewModel.onAction(ActionWeightInput.ChangeSystem(UnitSystemUnits.IMPERIAL))

            viewModel.onAction(ActionWeightInput.PoundsInput(200))

            val item1 = awaitItem()

            with(item1 as WeightSettingUiState.Imperial) {
               assertThat(kg).isCloseTo(91.0, 0.5)
                assertEquals(pounds, 200)
            }
            viewModel.onAction(ActionWeightInput.ChangeSystem(UnitSystemUnits.SI))

            val item2 = awaitItem()

            with(item2 as WeightSettingUiState.SI) {
                assertThat(kg).equals(91.0)
            }
            viewModel.onAction(ActionWeightInput.ChangeSystem(UnitSystemUnits.IMPERIAL))
            val item3 = awaitItem()

            with(item3 as WeightSettingUiState.Imperial) {
                assertThat(kg).isCloseTo(91.0, 0.5)
                assertEquals(pounds, 200)
            }

        }
    }

}