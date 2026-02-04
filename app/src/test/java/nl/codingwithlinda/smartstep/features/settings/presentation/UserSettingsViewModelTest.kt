package nl.codingwithlinda.smartstep.features.settings.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.HeightSettingsViewModel
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.HeightUnitConverter
import nl.codingwithlinda.smartstep.tests.FakeUserSettingsRepo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserSettingsViewModelTest {
    val testDispatcher = StandardTestDispatcher()
    val repo = FakeUserSettingsRepo()
    lateinit var viewModel: HeightSettingsViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HeightSettingsViewModel(
            repo,
            HeightUnitConverter()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test UserSettingsViewModel - userSettings are updated after sytem choice`() = runTest(testDispatcher){
        viewModel.heightUiState.test {
            val item0 = awaitItem()
            assertTrue(item0 is HeightSettingUiState.SI)

            viewModel.handleHeightInput(ActionHeightInput.ChangeUnitSystem(UnitSystemUnits.SI))

            val item1 = awaitItem()
            assertTrue(item1 is HeightSettingUiState.SI)

            viewModel.handleHeightInput(ActionHeightInput.CmInput(180))


            val item2 = awaitItem()
            assertTrue(item2 is HeightSettingUiState.SI).also {
                with(item2 as HeightSettingUiState.SI){
                    assertThat(this.valueCm).isEqualTo(180)
                }
            }

            viewModel.handleHeightInput(ActionHeightInput.ChangeUnitSystem(UnitSystemUnits.IMPERIAL))

            val item3 = awaitItem()
            println("item3: $item3")
            assertTrue(item3 is HeightSettingUiState.Imperial).also{
                with(item3 as HeightSettingUiState.Imperial){

                    assertThat(feet).isEqualTo(5)
                    assertThat(inches).isEqualTo(11)
                }
            }

        }
    }

}