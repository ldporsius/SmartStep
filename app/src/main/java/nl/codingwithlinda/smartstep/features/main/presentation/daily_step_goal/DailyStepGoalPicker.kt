package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.design.ui.theme.secondary

@Composable
fun DailyStepGoalPicker(
    goals: List<Int>,
    selectedGoal: Int,
    onGoalSelected: (Int) -> Unit,
    modifier: Modifier = Modifier) {


    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(goals){goal ->
            val isSelected = goal == selectedGoal
            val bgColor = if (isSelected) secondary else Color.Transparent
            val colorAnimation = animateColorAsState(
                targetValue = bgColor,
                animationSpec = tween(durationMillis = 50)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        onGoalSelected(goal)
                    }
                    .background(color = colorAnimation.value, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    "$goal",

                    )
            }
        }
    }
}