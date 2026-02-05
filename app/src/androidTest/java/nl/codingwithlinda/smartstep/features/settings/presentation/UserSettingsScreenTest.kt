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
import nl.codingwithlinda.smartstep.core.domain.model.settings.Gender
import nl.codingwithlinda.smartstep.core.domain.model.settings.UserSettings
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

    val robot = TestUserScreenRobot(composeRule)

    @Before
    fun setUp() {
        usersettingsRepo = FakeUserSettingsRepo()
        UserSettingsMemento.clear()
        UserSettingsMemento.save(UserSettings())
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
        UserSettingsMemento.clear()
    }

    @Test
    fun testUserSettingsScreen_updateStart() = runBlocking{
        composeRule.waitUntilExactlyOneExists(
            isRoot()
        )

        assertTrue(UserSettingsMemento.restoreLast() == UserSettings())

        assertEquals(UserSettingsMemento.restoreLast().gender, Gender.FEMALE)
        robot.pickGender()
        assertEquals(UserSettingsMemento.restoreLast().gender, Gender.MALE)

        robot.pickHeight().pressOK()
        assertEquals(UserSettingsMemento.restoreLast().height, 177)

        robot.pressStart()

        composeRule.awaitIdle()

        with(usersettingsRepo.loadSettings()){
            assertEquals(gender, Gender.MALE)
            assertEquals(height, 177)
        }


    }
    @Test
    fun testUserSettingsScreen_updateSkipped() = runBlocking{
        composeRule.waitUntilExactlyOneExists(
            isRoot()
        )

        assertTrue(UserSettingsMemento.restoreLast() == UserSettings())

        assertEquals(UserSettingsMemento.restoreLast().gender, Gender.FEMALE)
        robot.pickGender()
        assertEquals(UserSettingsMemento.restoreLast().gender, Gender.MALE)

        robot.pickHeight().pressOK()
        assertEquals(UserSettingsMemento.restoreLast().height, 177)

        robot.pressSkip()

        with(usersettingsRepo.loadSettings()){
            assertEquals(gender, Gender.FEMALE)
            assertEquals(height, 170)
        }


    }

}