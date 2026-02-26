package nl.codingwithlinda.smartstep.milestone2

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.uiautomator.UiDevice

class Milestone2Robot(
    val composeTestRule: ComposeTestRule,
    val device: UiDevice
) {

    @OptIn(ExperimentalTestApi::class)
    fun pretendWalking(): Milestone2Robot{

        composeTestRule.waitUntil(
            timeoutMillis = 10_000,
            condition = {
                composeTestRule.onNodeWithContentDescription("steps_taken").assertTextEquals("4").isDisplayed()
            }
        )
        return this

    }
}