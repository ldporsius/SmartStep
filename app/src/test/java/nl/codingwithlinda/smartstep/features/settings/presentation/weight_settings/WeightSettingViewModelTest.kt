package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.WeightUnitConverter
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingAction
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
            weightUnitConverter = WeightUnitConverter()
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

            viewModel.onAction(WeightSettingAction.KgInput(100))

            assertEquals(awaitItem(), WeightSettingUiState.SI(100))

            viewModel.onAction(WeightSettingAction.ChangeSystem(UnitSystemUnits.IMPERIAL))


            val item1 = awaitItem()
            assertTrue(item1 is WeightSettingUiState.Imperial)
            assertEquals(item1, WeightSettingUiState.Imperial(100))
            with(item1 as WeightSettingUiState.Imperial) {
                assertEquals(pounds, 220)
            }
        }
    }

}