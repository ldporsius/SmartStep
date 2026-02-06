package nl.codingwithlinda.smartstep.features.main.presentation

import android.Manifest
import android.app.Application
import android.os.UserHandle
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.MainActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var device: UiDevice

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    }

    @After
    fun tearDown() {
        val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName
        println("revoking permission for packageName: $packageName")
        InstrumentationRegistry.getInstrumentation().uiAutomation.revokeRuntimePermission(
            packageName,
            Manifest.permission.BODY_SENSORS,
        )
        //device.executeShellCommand("pm clear $packageName")
    }

    @Test
    fun basicTest(){
        composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)

    }

    @Test
    fun testBodySensorsPermissionDeclined(){
        val scenario = composeTestRule.activityRule.scenario

        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.moveToState(Lifecycle.State.STARTED)

       composeTestRule.waitForIdle()
        val dontAllowButton = device.findObject(UiSelector().textContains("Niet"))

        dontAllowButton.click()

        composeTestRule.waitUntilAtLeastOneExists(
            hasText("Allow access", substring = false, ignoreCase = true)
        )
        composeTestRule.onNode(hasText("Allow access", substring = false, ignoreCase = true))
            .assertIsDisplayed()
            .performClick()

        val dontAllowButton2 = device.findObject(UiSelector().textContains("Niet"))

        dontAllowButton2.click()

        composeTestRule.waitForIdle()

        composeTestRule.onNode(
            hasText("Open settings", substring = false, ignoreCase = true)
        ).assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()


        device.pressBack()

        composeTestRule.onNode(
            hasText("Open settings", substring = false, ignoreCase = true)
        ).assertDoesNotExist()

    }
}