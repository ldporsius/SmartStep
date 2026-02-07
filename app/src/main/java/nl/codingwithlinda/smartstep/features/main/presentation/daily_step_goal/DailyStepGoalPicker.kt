package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection

@Composable
fun DailyStepGoalPicker(
    goals: List<Int>,
    selectedGoal: Int,
    onGoalSelected: (Int) -> Unit,
    modifier: Modifier = Modifier) {

    val nestedScrollConn = rememberNestedScrollInteropConnection()
    LazyColumn(
        modifier = modifier
            //.nestedScroll(nestedScrollConn)
    ) {
        items(goals){
            goal ->
            Text("$goal")
        }
    }
}