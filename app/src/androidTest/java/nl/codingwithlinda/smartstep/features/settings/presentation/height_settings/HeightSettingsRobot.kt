package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import kotlinx.coroutines.delay

class HeightSettingsRobot(
    private val composeRule: ComposeTestRule
) {

    suspend fun waitForIdle(): HeightSettingsRobot{
        composeRule.awaitIdle()
        return this
    }
    fun changeToSI(): HeightSettingsRobot{
        composeRule.onNode(
            hasText("cm") and hasClickAction()
        ).performClick()
        return this
    }
    fun changeToImperial(): HeightSettingsRobot{
        composeRule.onNode(
            hasText("ft/in") and hasClickAction()
        ).performClick()
        return this
    }
    fun scrollToIndex(index: Int, label: String): HeightSettingsRobot{
        composeRule.onNodeWithContentDescription(
            "Number Picker $label"
        ).performScrollToIndex(index)
        return this

    }
    fun assertTextIsDisplayed(text: String): HeightSettingsRobot{
        composeRule.onNodeWithText(text).assertIsDisplayed()
        return this
    }
    fun assertContentDescriptionIsDisplayed(text: String): HeightSettingsRobot{
        composeRule.onNodeWithContentDescription(text, substring = true, ignoreCase = true).assertIsDisplayed()
        return this
    }

    suspend fun keepOnScreen(duration: Long = 1000): HeightSettingsRobot {
        delay(duration)
        return this
    }
}