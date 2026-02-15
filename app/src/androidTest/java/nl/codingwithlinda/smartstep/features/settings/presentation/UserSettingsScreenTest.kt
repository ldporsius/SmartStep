package nl.codingwithlinda.smartstep.features.settings.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.core.domain.model.settings.Gender
import nl.codingwithlinda.smartstep.core.domain.model.settings.UserSettings
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.heightsCm
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.kgToPounds
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.kgToPoundsFactor
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.weightRangePounds
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.onboarding.presentation.UserSettingsOnboardingWrapper
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
                UserSettingsOnboardingWrapper(
                    onSkip = {

                    },
                   action = {
                       runBlocking {
                           UserSettingsMemento.restoreLast().also {
                               usersettingsRepo.saveSettings(it)
                           }

                       }
                    }
                ) {
                    UserSettingsRoot(
                        userSettingsRepo = usersettingsRepo,

                        )
                }
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

        robot.pickHeight(
            initialHeight = 170,
        ).scrollToIndex(heightsCm.indexOf(180), "cm")
            .pressOK()
        assertEquals(UserSettingsMemento.restoreLast().heightCm, 180)

        robot.pressStart()

        composeRule.awaitIdle()

        with(usersettingsRepo.loadSettings()){
            assertEquals(gender, Gender.MALE)
            assertEquals(heightCm, 180)
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

        robot.pickHeight()
            .scrollToIndex(heightsCm.indexOf(177), "cm")
            .pressOK()
        assertEquals(UserSettingsMemento.restoreLast().heightCm, 177)

        robot.pressSkip()

        with(usersettingsRepo.loadSettings()){
            assertEquals(gender, Gender.FEMALE)
            assertEquals(heightCm, 170)
        }
    }

    @Test
    fun test_usersettings_switchSI_Imperial(): Unit = runBlocking {
        composeRule.waitUntilExactlyOneExists(
            isRoot()
        )
        robot.pickWeight()
            .selectImperial("lbs")
            .scrollToIndex(weightRangePounds.indexOf(200), "lbs")

        composeRule.waitForIdle()
        robot.selectSI("kg")
            .selectImperial("lbs")

        composeRule.waitForIdle()
        composeRule.onNodeWithText(
            "200"
        ).assertIsDisplayed()

        composeRule.onNodeWithContentDescription(
            "Label 200"
        ).assertIsDisplayed()

        robot .pressOK()
            .pressStart()

        assertEquals(usersettingsRepo.loadSettings().weightGrams, (200 / kgToPoundsFactor) * 1000, .5)

        robot.pickWeight()
            .selectSI("kg")

        composeRule.onNodeWithContentDescription(
            "Label 91"
        ).assertIsDisplayed()

    }

}