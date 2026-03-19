package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import nl.codingwithlinda.smartstep.FakeUserSettingsRepo
import nl.codingwithlinda.smartstep.FakeUserStatisticsRepo
import nl.codingwithlinda.smartstep.FakeWalkDurationRepo
import nl.codingwithlinda.smartstep.features.weekly_activity_report.data.WeeklyStatisticsManager
import nl.codingwithlinda.smartstep.util.BaseStepRepoTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeeklyActivityReportScreenTest: BaseStepRepoTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    val viewModel = ReportViewModel(
        weeklyStatisticsManager = WeeklyStatisticsManager(
            userStatisticsRepo = FakeUserStatisticsRepo(),
            dailyStepRepo = fakeDailyStepRepo,
            walkDurationRepo = FakeWalkDurationRepo()
        ),
        userSettingsRepo = FakeUserSettingsRepo()
        )

    @Before
    override fun setup() {
        super.setup()
        composeTestRule.setContent {
            WeeklyActivityReportScreen(
                reportViewModel = viewModel,
                onNavBack = {}
            )
        }
    }

    @Test
    fun testWeeklyActivity_StepIsDisplayed(){
        composeTestRule.onNodeWithText("Steps").assertIsDisplayed()

    }

}