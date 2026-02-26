package nl.codingwithlinda.smartstep.features.main.util

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

class TestPermissionsRobot(
    val testRule: ComposeTestRule,
    val device: UiDevice
) {
    fun clickOnDeny(): TestPermissionsRobot{
        device.findObject(UiSelector().textStartsWith("Niet")).click()
        testRule.waitForIdle()
        return this
    }
    @OptIn(ExperimentalTestApi::class)
    fun clickOnAllow(): TestPermissionsRobot {
        testRule.waitUntilExactlyOneExists(
            hasText("Allow", substring = true, ignoreCase = true) and hasClickAction()
        )
        testRule . onNode (
            hasText("Allow", substring = true, ignoreCase = true) and hasClickAction()
        ).performClick()

        return this
    }

    fun openSettings(): TestPermissionsRobot{
        testRule . onNode (
            hasText("Open", substring = true, ignoreCase = true) and hasClickAction()
        ).performClick()

        return this
    }

    fun allowRecognitionManually(): TestPermissionsRobot{
        device.findObject(UiSelector().textStartsWith("Toestemming")).click()
        testRule.waitForIdle()
        device.findObject(UiSelector().textStartsWith("Fysieke")).click()
        testRule.waitForIdle()
        device.findObject(UiSelector().textStartsWith("Toestaan")).click()
        testRule.waitForIdle()
        device.pressBack()
        device.pressBack()

        return this

    }
}