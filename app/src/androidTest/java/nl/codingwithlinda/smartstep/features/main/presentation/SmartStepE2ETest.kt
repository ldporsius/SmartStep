package nl.codingwithlinda.smartstep.features.main.presentation

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.MainActivity
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.dataStore
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmartStepE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>(
        effectContext = SmartStepApplication.applicationScope.coroutineContext
    )

    val context: SmartStepApplication = ApplicationProvider.getApplicationContext<SmartStepApplication>()

    @Before
    fun setUp(){
        runBlocking {
            context.dataStore.edit {
                it.clear()
            }
        }
    }
    @Ignore
    @Test
    fun smartStepE2E_permissionsGranted() {
        //composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)


        val mainActivity = composeTestRule.activity
        mainActivity.isChecking = false



        composeTestRule.onNode(hasText("Start")).assertIsDisplayed().performClick()

    }

}