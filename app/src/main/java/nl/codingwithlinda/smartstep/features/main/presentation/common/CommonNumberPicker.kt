package nl.codingwithlinda.smartstep.features.main.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import nl.codingwithlinda.smartstep.design.ui.theme.secondary

@Composable
fun CommonNumberPicker(
    label: String,
    values: List<Int>,
    selectedGoal: Int,
    onGoalSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
   ) {

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = values.indexOf(selectedGoal)
    )
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val snap = snapshotFlow {
        listState.firstVisibleItemIndex
    }

    LaunchedEffect(Unit){
        listState.scrollToItem(values.indexOf(selectedGoal))
    }
    LaunchedEffect(true,listState){
        snap.onEach {
            onGoalSelected(values.getOrNull(it) ?: 0)
        }.collect()
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        ConstraintLayout() {
            val (numberRef,
                centerBox,
                labelRef) = createRefs()

            val centerGuidelineHor = createGuidelineFromTop(.5f)
            val centerGuidelineVer = createGuidelineFromStart(.5f)

            Spacer(
                modifier = Modifier
                    .constrainAs(centerBox) {
                        this.centerAround(centerGuidelineHor)
                    }
                    .fillMaxWidth()
                    .height(58.dp)
                    .background(color = secondary, shape = RoundedCornerShape(0.dp))
                ,
            )
            LazyColumn(
                modifier = Modifier
                    .semantics(){
                        contentDescription = "Number Picker $label"
                    }
                    .constrainAs(numberRef) {
                        end.linkTo(centerGuidelineVer)
                    }
                    .height(250.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                contentPadding = PaddingValues(vertical = 1.dp),
                flingBehavior = snapFlingBehavior,
                state = listState
            ) {
                items(2) {
                    Box(
                        modifier = Modifier
                            .height(50.dp)

                    ) {
                        Text(
                            "",
                        )
                    }
                }
                items(values) { goal ->

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.CenterEnd

                    ) {
                        Text("$goal",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .semantics(){
                                    contentDescription = "value $label"
                                }

                        )
                    }
                }

                items(2) {
                    Box(
                        modifier = Modifier
                            .height(50.dp)

                    ) {
                        Text(
                            "",
                        )
                    }
                }
            }


           /* Text(
                "${centerGoalText}",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.constrainAs(centerNumber) {
                    centerAround(centerGuidelineHor)
                    end.linkTo(
                        centerGuidelineVer
                    )
                }
            )*/

            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.constrainAs(labelRef) {
                    centerAround(centerGuidelineHor)
                    start.linkTo(
                       anchor =  numberRef.end,
                        margin = (16).dp
                    )
                }
                    .semantics(){
                        contentDescription = "Label $selectedGoal"
                    }
            )

        }

    }
}