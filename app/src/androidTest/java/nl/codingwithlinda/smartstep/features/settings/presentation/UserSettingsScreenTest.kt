package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.core.domain.model.Gender
import nl.codingwithlinda.smartstep.core.domain.model.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.tests.FakeUserSettingsRepo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class UserSettingsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    lateinit var usersettingsRepo: UserSettingsRepo

    @Before
    fun setUp() {
        usersettingsRepo = FakeUserSettingsRepo()
        composeRule.setContent {
            SmartStepTheme {
                UserSettingsRoot(
                    userSettingsRepo = usersettingsRepo,
                    actionSkip = {
                        println("action skip called")
                    }
                )
            }
        }
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testUserSettingsScreen() = runBlocking{
        composeRule.waitUntilExactlyOneExists(
            isRoot()
        )

        assertTrue(UserSettingsMemento.restoreLast() == UserSettings())

        composeRule.onNode(
            hasContentDescription("Gender") and hasClickAction()
        ).performClick()
        composeRule.waitUntilExactlyOneExists(
            hasText("Man")
        ).also {
            composeRule.onNodeWithText("Man").performClick()
        }
        composeRule.awaitIdle()
        assertEquals(UserSettingsMemento.restoreLast().gender, Gender.MALE)

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
        composeRule.awaitIdle()
        composeRule.onNodeWithText("OK").performClick()
        composeRule.awaitIdle()

        assertEquals(UserSettingsMemento.restoreLast().height, 177)


        composeRule.onNodeWithText("Skip").performClick()
        composeRule.awaitIdle()
        with(usersettingsRepo.loadSettings()){
            assertEquals(gender, Gender.FEMALE)
            assertEquals(height, 170)
        }


        delay(5000)



    }

}