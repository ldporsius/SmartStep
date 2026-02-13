package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp

@OptIn(ExperimentalTestApi::class)
class TestUserScreenRobot(
    private val composeRule: ComposeTestRule

) {

    suspend fun pickGender(): TestUserScreenRobot {
        composeRule.onNode(
            hasContentDescription("Gender") and hasClickAction()
        ).performClick()
        composeRule.waitUntilExactlyOneExists(
            hasText("Man")
        ).also {
            composeRule.onNodeWithText("Man").performClick()
        }
        composeRule.awaitIdle()
        return this
    }

    fun pickHeight(initialHeight: Int = 170, ): TestUserScreenRobot {
        composeRule.onNode(
            hasContentDescription("Height") and hasClickAction()
        ).performClick()

        composeRule.waitUntilExactlyOneExists(
            hasText("$initialHeight")
        )

        return this

    }
    fun pickWeight(): TestUserScreenRobot {
        composeRule.onNode(
            hasText("Weight") and hasClickAction()
        ).performClick()

        return this
    }
    fun performSwipe(targetHeight: Int = 180): TestUserScreenRobot {
        composeRule.onNode(
            hasContentDescription("Number Picker")
        ).assertIsDisplayed()
            .performTouchInput {
                try {
                    swipeUp(500f , 10f, 2000)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

        composeRule.waitUntilExactlyOneExists(
            hasText("$targetHeight")
        )

        return this
    }
    fun scrollToIndex(index: Int, label: String): TestUserScreenRobot {
        composeRule.onNode(
            hasContentDescription("Number Picker $label")
        ).assertIsDisplayed()
            .performScrollToIndex(index)

        return this
    }

    fun selectImperial(description: String): TestUserScreenRobot {
        composeRule.onNode(
            hasText(description) and hasClickAction()
        ).performClick()
        return this
    }
    fun selectSI(description: String): TestUserScreenRobot {
        composeRule.onNode(
            hasText(description)
        ).performClick()
        return this
    }

    suspend fun pressOK(): TestUserScreenRobot {
        composeRule.awaitIdle()
        composeRule.onNodeWithText("OK").performClick()
        composeRule.awaitIdle()

        return this
    }
    suspend fun pressCancel(): TestUserScreenRobot{
        composeRule.awaitIdle()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.awaitIdle()
        return this
    }

    suspend fun pressSkip(): TestUserScreenRobot{
        composeRule.awaitIdle()
        composeRule.onNodeWithText("Skip").performClick()
        composeRule.awaitIdle()
        return this
    }
    suspend fun pressStart(): TestUserScreenRobot{
        composeRule.awaitIdle()
        composeRule.onNodeWithText("Start").performClick()
        composeRule.awaitIdle()
        return this
    }

}