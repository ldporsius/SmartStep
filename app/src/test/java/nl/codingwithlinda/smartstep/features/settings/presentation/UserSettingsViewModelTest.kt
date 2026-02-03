package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.lifecycle.viewmodel.compose.viewModel
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.features.settings.presentation.UserSettingsViewModel
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
    lateinit var viewModel: UserSettingsViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserSettingsViewModel(
                repo,
        HeightUnitConverter()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test UserSettingsViewModel - zzz`()= runTest(testDispatcher){
        viewModel.heightUiState.test {
            val item0 = awaitItem()
            assertTrue(item0 is HeightSettingUiState.Imperial)
        }
    }

}