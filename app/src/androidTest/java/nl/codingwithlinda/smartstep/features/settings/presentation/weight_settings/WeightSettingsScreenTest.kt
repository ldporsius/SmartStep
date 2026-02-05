package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

import android.app.Application
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.weightRangeKg
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.weightRangePounds
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.ActionWeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state.WeightSettingUiState
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@OptIn(ExperimentalTestApi::class)
class WeightSettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    val uiState = MutableStateFlow<WeightSettingUiState> ( WeightSettingUiState.SI(weightRangeKg.last()) )
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    val context = ApplicationProvider.getApplicationContext<Application>()
    val screenshotPath = File(context.cacheDir, "testWeightSettingsScreen.png")

    @Before
    fun setUp() {

        composeTestRule.setContent {
            WeightSettingsScreen(
                uiState = uiState.collectAsStateWithLifecycle().value,
                valuesKg = weightRangeKg.toList(),
                valuesPounds = weightRangePounds,
                action = {
                    when (it) {
                       is ActionWeightInput.ChangeSystem ->{
                           uiState.update {
                               WeightSettingUiState.Imperial(weightRangeKg.last())
                           }

                       }
                    }
                },
                onSave = {  },
                onCancel = {  },
                modifier = Modifier
            )

        }
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testWeightSettingsScreen(): Unit = runBlocking {

        composeTestRule.waitUntilExactlyOneExists(
            isRoot()
        )
        composeTestRule.onNode(
            hasText("kg") and hasClickAction()
        ).assertExists()
            .assertIsSelectable()
            .assertIsSelected()

        composeTestRule.onNodeWithText(weightRangeKg.last().toString()).assertExists()

        composeTestRule.waitUntilAtLeastOneExists(
            hasText("lbs") and hasClickAction()
        )
        composeTestRule.onNode(
            hasText("lbs") and isSelectable()
        ).assertExists()
            .performClick()

        composeTestRule.awaitIdle()

        composeTestRule.waitUntilExactlyOneExists(
            hasText("lbs") and isSelected(),
            timeoutMillis = 3000

        )
        println("Pounds tab was clicked")

        device.takeScreenshot(screenshotPath)

        composeTestRule.waitUntilAtLeastOneExists(
            hasText(weightRangePounds.last().toString()),
            timeoutMillis = 3000
        )
        composeTestRule.onNode(
            hasText(weightRangePounds.last().toString())
        ).assertIsDisplayed()


        delay(5000)

    }
}