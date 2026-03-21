package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import kotlinx.coroutines.flow.firstOrNull
import nl.codingwithlinda.smartstep.FakeActivityRecognitionRepo
import nl.codingwithlinda.smartstep.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.FakeUserSettingsRepo
import nl.codingwithlinda.smartstep.FakeUserStatisticsRepo
import nl.codingwithlinda.smartstep.FakeWalkDurationRepo
import nl.codingwithlinda.smartstep.features.weekly_activity_report.data.WeeklyStatisticsManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class WeeklyActivityReportScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val repo = FakeActivityRecognitionRepo()
    val stepsTaken = repo.stepCount
    val dailyStepRepo = FakeDailyStepRepo(
        dateToday = LocalDate.now(),
        stepsTaken = stepsTaken,
        getStepCountForDate = {
           repo.getStepCountForDate(LocalDate.now().toEpochDay())
        }
    )
    val viewModel = ReportViewModel(
        weeklyStatisticsManager = WeeklyStatisticsManager(
            userStatisticsRepo = FakeUserStatisticsRepo(),
            dailyStepRepo = dailyStepRepo,
            walkDurationRepo = FakeWalkDurationRepo()
        ),
        userSettingsRepo = FakeUserSettingsRepo()
        )

    @Before
    fun setup() {

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