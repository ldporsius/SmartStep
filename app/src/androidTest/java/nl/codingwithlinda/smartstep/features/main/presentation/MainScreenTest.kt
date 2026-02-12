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
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.platform.content.PermissionGranter
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnitRunner
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.MainActivity
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.dataStore
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>(
        effectContext = SmartStepApplication.applicationScope.coroutineContext
    )
    //@get:Rule
    //val allowNotifications: GrantPermissionRule? = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
    val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName
    val context: SmartStepApplication = ApplicationProvider.getApplicationContext<SmartStepApplication>()


    @Before
    fun setUp() {
        runBlocking {
            context.dataStore.edit {
                it.clear()
            }
        }
    }

    @After
    fun tearDown() {

    }


    @Test
    fun testBodySensorsPermissionDeclined(): Unit = runBlocking {

        if(Build.VERSION.SDK_INT >= 28){
            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                packageName,
                Manifest.permission.ACTIVITY_RECOGNITION,
            )

            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                packageName, Manifest.permission.POST_NOTIFICATIONS
            )
        }
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val mainActivity = composeTestRule.activity
        mainActivity.isChecking = false

        composeTestRule.onNode(
            hasText("Start") and hasClickAction()
        ).assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntilAtLeastOneExists(
            hasContentDescription("Daily Step Card")
        )

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