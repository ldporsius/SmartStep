package nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavItemHandler
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test


@Ignore
@OptIn(ExperimentalTestApi::class)
class MainScreenDecoratorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup(){
        composeTestRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                MainScreenDecorator(
                    mainNavAction = MainNavAction.DAILY_STEP_GOAL,
                    navItemHandler = MainNavItemHandler,
                    parent = this

                )
            }
        }
    }

    @Test
    fun testMainScreenDecorator() : Unit = runBlocking{
        composeTestRule.waitUntilAtLeastOneExists(
            isRoot()
        )
        delay(5000)

        composeTestRule.waitForIdle()


        composeTestRule.onNodeWithText("goal, true, true").assertIsDisplayed()


    }

}