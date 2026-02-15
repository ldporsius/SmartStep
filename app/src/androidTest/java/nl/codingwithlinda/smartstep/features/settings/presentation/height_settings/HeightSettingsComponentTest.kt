package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import assertk.assertThat
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.HeightUnitConverter
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsCm
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsInches
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightCm
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightInches
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.ActionHeightInput
import nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state.HeightSettingUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt


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
                action = {action ->
                    when (action) {
                        is ActionHeightInput.ChangeUnitSystem -> {
                            when(action.system){
                                UnitSystems.IMPERIAL -> {
                                    uiState.update {
                                        HeightSettingUiState.Imperial(it.valueCm)
                                    }
                                }
                                UnitSystems.SI -> {
                                    uiState.update {
                                        HeightSettingUiState.SI(it.valueCm)
                                    }
                                }
                            }
                        }

                        ActionHeightInput.ActionSave -> Unit
                        is ActionHeightInput.CmInput -> {
                            val cm = action.cm
                            uiState.update {
                                HeightSettingUiState.SI(cm)
                            }
                        }
                        is ActionHeightInput.ImperialInput -> {
                            val feet = action.feet
                            val inches = action.inches
                            uiState.update {
                                HeightSettingUiState.Imperial(
                                    valueCm = HeightUnitConverter.toSI(feet, inches).roundToInt()
                                )
                            }
                        }
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

    @Test
    fun testSwitchingHeightFromSIToImperial(): Unit = runBlocking{
        val robot = HeightSettingsRobot(composeRule)
        val maxIndexCm = heightsCm.indexOf(maxHeightCm)
        val maxIndexFeet = heightsFeet.indexOf(maxHeightFeet)
        val maxIndexInches = heightsInches.indexOf(maxHeightInches)

        assertThat(uiState.value).isInstanceOf(HeightSettingUiState.SI::class)

        println("----------maxCm---------------: $maxHeightCm")
        println("----------maxIndexCm---------------: $maxIndexCm")

        composeRule.awaitIdle()
        robot
            .changeToSI()
            .waitForIdle()
            .scrollToIndex(maxIndexCm, "cm")
            .waitForIdle()
            .keepOnScreen(5000)
            .assertContentDescriptionIsDisplayed("Label $maxHeightCm")
            .changeToImperial()
            .waitForIdle()
            .assertContentDescriptionIsDisplayed("Label $maxHeightFeet")
            .keepOnScreen(3000)
            .scrollToIndex(maxIndexInches, "in")
            .waitForIdle()
            .assertContentDescriptionIsDisplayed("Label $maxHeightInches")

            .changeToSI()
            .waitForIdle()
            .assertContentDescriptionIsDisplayed("Label $maxHeightCm")
            .keepOnScreen(3000)




    }

}