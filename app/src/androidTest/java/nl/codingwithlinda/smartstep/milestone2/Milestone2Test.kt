package nl.codingwithlinda.smartstep.milestone2

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.MainActivity
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import org.junit.Rule
import org.junit.Test

class Milestone2Test {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>(
        effectContext = SmartStepApplication.applicationScope.coroutineContext
    )
    val packageName: String = InstrumentationRegistry.getInstrumentation().targetContext.packageName


    @Test
    fun milestone2Test() = runBlocking {
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



    }

}