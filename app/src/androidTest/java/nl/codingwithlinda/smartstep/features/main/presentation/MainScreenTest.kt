package nl.codingwithlinda.smartstep.features.main.presentation

import android.Manifest
import android.app.Application
import android.app.UiAutomation
import android.os.Build
import android.os.UserHandle
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.platform.content.PermissionGranter
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnitRunner
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
import org.junit.runner.RunWith
import java.io.File

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    //@get:Rule
    //val allowNotifications: GrantPermissionRule? = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
    val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName


    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }


    @Test
    fun testBodySensorsPermissionDeclined() {
        //val scenario = composeTestRule.activityRule.scenario

        if(Build.VERSION.SDK_INT >= 28){
            InstrumentationRegistry.getInstrumentation().uiAutomation.revokeRuntimePermission(
                packageName,
                Manifest.permission.ACTIVITY_RECOGNITION,
            )

            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                packageName, Manifest.permission.POST_NOTIFICATIONS
            )
        }
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        composeTestRule.onNode(
            hasText("Start") and hasClickAction()
        ).assertIsDisplayed()
            .performClick()


        val dontAllowButton = device.findObject(UiSelector().textContains("Niet"))

        dontAllowButton.click()


        composeTestRule.onNode(hasText("Allow access", substring = false, ignoreCase = true))
            .assertIsDisplayed()
            .performClick()


        val dontAllowButton2 = device.findObject(UiSelector().textContains("Niet"))

        dontAllowButton2.click()


        composeTestRule.onNode(
            hasText("Open settings", substring = false, ignoreCase = true)
        ).assertIsDisplayed()
            .performClick()


        device.pressBack()

        composeTestRule.onNode(
            hasText("Open settings", substring = false, ignoreCase = true)
        ).assertDoesNotExist()

    }
}