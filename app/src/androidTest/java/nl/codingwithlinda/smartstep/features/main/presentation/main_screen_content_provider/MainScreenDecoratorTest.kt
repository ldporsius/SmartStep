package nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavActionController
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavActionControllerImpl
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.tests.FakeSmartStepStateController
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalTestApi::class)
class MainScreenDecoratorTest {


    @get:Rule
    val composeTestRule = createComposeRule(
        //effectContext = SmartStepApplication.applicationScope.coroutineContext
    )

    val smartStepStateController = FakeSmartStepStateController()

    val dailyStepGoalViewModel = DailyStepGoalViewModel(
        appScope = CoroutineScope(StandardTestDispatcher()),
        dailyStepRepo = FakeDailyStepRepo()
    )

    val fakeNavActionController = object : MainNavActionController {
        override fun handleAction(action: MainNavAction) {
            println("fake nav action controller handle action")
        }
    }

    @Before
    fun setup(){
        composeTestRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                MainNavItemHandler(
                    dailyStepGoalViewModel = dailyStepGoalViewModel,
                    mainNavAction = MainNavAction.DAILY_STEP_GOAL,
                    navItemHandler = fakeNavActionController,
                    smartStepStateController = smartStepStateController,
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