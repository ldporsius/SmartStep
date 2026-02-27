package nl.codingwithlinda.smartstep.features.main.presentation

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.MainActivity
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.features.main.util.TestPermissionsRobot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>(
       // effectContext = SmartStepApplication.applicationScope.coroutineContext
    )
    //@get:Rule
    //val allowNotifications: GrantPermissionRule? = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
    //val context: SmartStepApplication = ApplicationProvider.getApplicationContext<SmartStepApplication>()
    val packageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName


    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {

    }


    @Test
    fun testActivityRecognitionPermissionDeclined() = runBlocking{

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
        val mainActivity = composeTestRule.activity
        mainActivity.isChecking = false

        val robot = TestPermissionsRobot(
            testRule = composeTestRule,
            device = device
        )

        composeTestRule.waitUntilAtLeastOneExists(
            isRoot()
        )

        composeTestRule . onNodeWithText ("Start").performClick()

        composeTestRule . waitForIdle ()

        delay (1000)

        robot.clickOnDeny()

        delay(1000)
        robot.clickOnAllow()

        delay (1000)
        robot.clickOnDeny()

        delay(1000)
        robot.openSettings()
            .allowRecognitionManually()


        delay (1000)


    }
}