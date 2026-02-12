package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import nl.codingwithlinda.smartstep.design.ui.theme.secondary

@Composable
fun DailyStepGoalPicker(
    goals: List<Int>,
    selectedGoal: Int,
    onGoalSelected: (Int) -> Unit,
    modifier: Modifier = Modifier) {

    val listState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val snap = snapshotFlow {
        listState.firstVisibleItemIndex
    }


    var centerGoalText by remember {
        mutableStateOf(
        listState.firstVisibleItemIndex.let {
            "${goals.getOrNull(it)} "
        }
        )
    }


    LaunchedEffect(Unit){
        listState.animateScrollToItem(goals.indexOf(selectedGoal))
    }
    LaunchedEffect(true,listState){
        snap.onEach {
            onGoalSelected(goals.getOrNull(it) ?: 0)
            centerGoalText = "${goals.getOrNull(it)} "
        }.collect()
    }
    Box() {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 1.dp),
            flingBehavior = snapFlingBehavior,
            state = listState
        ) {
            items(2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)

                ) {
                    Text(
                        "",
                    )
                }
            }
            items(goals) { goal ->
                val isSelected = goal == selectedGoal
                val bgColor = if (isSelected) secondary else Color.Transparent
                val colorAnimation = animateColorAsState(
                    targetValue = bgColor,
                    animationSpec = tween(durationMillis = 50)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                     , contentAlignment = Alignment.Center

                ) {
                    Text("$goal")
                }
            }

            items(2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)

                ) {
                    Text(
                        "",
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .background(color = Color.LightGray, shape = RoundedCornerShape(6.dp))
                .align(Alignment.Center),
            contentAlignment = Alignment.Center

        ) {
            Text("${centerGoalText}",
                style = MaterialTheme.typography.labelLarge)
        }

    }
}