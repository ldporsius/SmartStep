package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsCm
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsInches
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightCm
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.minHeightInches
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalTestApi::class)
class HeightSettingsComponentTest {

    @get:Rule
    val composeRule = createComposeRule()

    val uiState: MutableStateFlow<HeightSettingUiState> = MutableStateFlow(HeightSettingUiState.SI(maxHeightCm))

    @Before
    fun setUp() {
        composeRule.setContent {
            HeightSettingsComponent(
                uiState = uiState.collectAsStateWithLifecycle().value,
                rangeCm = heightsCm.toList(),
                rangeFeet = heightsFeet,
                rangeInches = heightsInches,
                action = {
                    when (it) {
                        is ActionHeightInput.ChangeUnitSystem -> {
                            uiState.update {
                                HeightSettingUiState.Imperial(maxHeightCm)
                            }
                        }

                        ActionHeightInput.ActionSave -> Unit
                        is ActionHeightInput.CmInput -> Unit
                        is ActionHeightInput.ImperialInput -> Unit
                    }
                },
                onCancel = {},
                onSave = {},
                modifier = Modifier
            )
        }
    }



    @Test
    fun testHeightSettingsComponent() : Unit = runBlocking {
        uiState.update {
            HeightSettingUiState.SI(maxHeightCm)
        }

        composeRule.awaitIdle()
        composeRule.onNode(
            hasText(maxHeightCm.toString())
        ).assertIsDisplayed()
        composeRule.waitUntilAtLeastOneExists(
            hasText("ft/in") and hasClickAction()
        )
        composeRule.onNode(
            hasText("ft/in") and hasClickAction()
        ).assertIsDisplayed()
            .performClick()

        composeRule.awaitIdle()

        composeRule.onNode(
            hasText(maxHeightFeet.toString()) and hasContentDescription("value ft")
        ).assertIsDisplayed()

        composeRule.onNode(
            hasText(2.toString()) and hasContentDescription("value in")
        ).assertIsDisplayed()


        delay(5000)


    }

}