package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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

    suspend fun pickHeight(): TestUserScreenRobot {
        composeRule.onNode(
            hasContentDescription("Height") and hasClickAction()
        ).performClick()

        composeRule.waitUntilExactlyOneExists(
            hasText("170")
        )

        composeRule.onNode(
            hasContentDescription("scroll to pick height")
        ).assertIsDisplayed()
            //these values happen to yield 177
            .performTouchInput {
                try {
                    swipeUp(500f , 100f, 2000)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

        composeRule.waitUntilExactlyOneExists(
            hasText("180")
        )

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