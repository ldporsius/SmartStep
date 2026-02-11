package nl.codingwithlinda.smartstep.features.main.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.codingwithlinda.smartstep.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmartStepE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun smartStepE2E_permissionsGranted(){

        composeTestRule.onNode(hasText("Start")).assertIsDisplayed().performClick()

    }

}