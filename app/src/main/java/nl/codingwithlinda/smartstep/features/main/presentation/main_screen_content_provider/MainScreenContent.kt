package nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.DailyStepCard
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.DailyStepCountViewModel
import nl.codingwithlinda.smartstep.features.statistics.presentation.StatisticsViewModel
import nl.codingwithlinda.smartstep.features.walk_duration.presentation.WalkDurationViewModel
import nl.codingwithlinda.smartstep.features.steps_override_user.navigation.StepNavActionHandler
import nl.codingwithlinda.smartstep.features.weekly_average.presentation.WeeklyAverageScreen
import nl.codingwithlinda.smartstep.features.weekly_average.presentation.WeeklyAverageViewModel

@Composable
fun MainScreenContent(
    dailyStepGoalViewModel: DailyStepGoalViewModel,
    dailyStepCountViewModel: DailyStepCountViewModel,
    statisticsViewModel: StatisticsViewModel,
    walkDurationViewModel: WalkDurationViewModel,
    weeklyAverageViewModel: WeeklyAverageViewModel
    ) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DailyStepCard(
            stepsTaken = dailyStepCountViewModel.stepsToday.collectAsStateWithLifecycle().value,
            dailyGoal = dailyStepGoalViewModel.goal.collectAsStateWithLifecycle().value,
            statisticsUi = statisticsViewModel.statistics.collectAsStateWithLifecycle().value,
            stepTrackerState = walkDurationViewModel.state.collectAsStateWithLifecycle().value,
            actionEdit = {
                StepNavActionHandler.handleAction(StepNavAction.EDIT_STEPS)
            },
            actionPause = {
                walkDurationViewModel.pause()
            },
            actionPlay = {
                walkDurationViewModel.start()
            },
            modifier = Modifier
                .semantics {
                    contentDescription = "Daily Step Card"
                }
                .padding(16.dp)
        )

        WeeklyAverageScreen(
            days = weeklyAverageViewModel.lastSevenStepCounts.collectAsStateWithLifecycle().value,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}