package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.design.ui.theme.SmartStepTheme
import kotlin.math.roundToInt

@Composable
fun ScrollableHeightInputComponent(
    label: String,
    defaultValue: Int,
    values: List<Int>,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier) {


    var selectionPosition by remember { mutableStateOf(0f) }
    var selectedValue by remember { mutableStateOf(defaultValue) }

    var valueTextHeight: Int by remember {
        mutableStateOf(126)
    }

    var centerY by remember {
        mutableStateOf(0f)
    }
    var YOffset by remember {
        mutableStateOf(0)
    }

    val indexInList = values.indexOf(selectedValue)
    val visibleRange =( (indexInList - 5).coerceAtLeast(0)..(indexInList + 5).coerceAtMost(values.size) ).toList()


    LaunchedEffect(valueTextHeight){
        println("centerY = $centerY")
        println("valueTextHeight = $valueTextHeight")
        println("indexInList = $indexInList")
        val indexInVisibleRange = visibleRange.indexOf(indexInList)
        println("indexInVisibleRange = $indexInVisibleRange")

        selectionPosition = centerY - valueTextHeight.toFloat() * indexInList

        println("selectedValue = $selectedValue")
        println("selectionPosition = $selectionPosition")

    }

    Box(modifier = modifier
        .width(100.dp),
        contentAlignment = Alignment.TopStart
    ) {

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.LightGray)
            .align(androidx.compose.ui.Alignment.Center)
            .onGloballyPositioned(){
                centerY = it.positionInParent().y
            }
        )

        val minOffsetY = centerY.toInt() - valueTextHeight * (values.size-1)
        val maxOffsetY = (centerY).toInt()

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(
                    state = rememberScrollState(),
                    enabled = false
                )
                .width(IntrinsicSize.Max)
                .offset{
                    IntOffset(0, (selectionPosition.toInt())
                        .coerceIn( minOffsetY, maxOffsetY))
                }
                .pointerInput(Unit){
                    detectVerticalDragGestures(
                        onDragEnd = {

                            YOffset = (selectionPosition).toInt()


                            try {
                                println("centerY = $centerY")
                                println("minOffsetY = $minOffsetY")
                                println("maxOffsetY = $maxOffsetY")
                                println("YOffset = $YOffset")

                                val listHeightPix = values.size * valueTextHeight

                                // Calculate normalized position (0 to 1)
                                val normalizedPosition =
                                    1f - ((YOffset - minOffsetY).toFloat() / (maxOffsetY - minOffsetY).toFloat())
                                println("normalizedPosition = $normalizedPosition")


                                val selectedIndex = (normalizedPosition * values.size)
                                    .roundToInt()
                                    .coerceIn(0, values.size - 1)
                                println("selectedIndex = $selectedIndex")

                                selectedValue = values[selectedIndex]
                                println("selectedValue = $selectedValue")

                                onValueChange(selectedValue)
                            }catch (e: Exception){
                                e.printStackTrace()
                            }
                        },
                        onVerticalDrag = { change, dragAmount ->
                            selectionPosition += dragAmount

                        }
                    )
                }
            ,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            values.onEach { value ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .onGloballyPositioned{
                            //valueTextHeight = it.size.height
                        }
                        .border(Dp.Hairline, Color.Black),
                    contentAlignment = androidx.compose.ui.Alignment.Center

                ) {
                    Text(
                        "$value",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Text(
            label,
            modifier = Modifier
                .align(Alignment.CenterEnd)
        )


    }
}

@Preview
@Composable
private fun PreviewHeightInchesComponent() {
    SmartStepTheme {
        ScrollableHeightInputComponent(
            label = "ft",
            defaultValue = 5,
            values = (0..10).toList(),
            onValueChange = {},
            modifier = Modifier.fillMaxHeight()

        )

    }
}