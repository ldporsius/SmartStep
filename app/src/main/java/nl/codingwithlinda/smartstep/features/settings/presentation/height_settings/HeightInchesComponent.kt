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
fun HeightInchesComponent(
    modifier: Modifier = Modifier) {


    var feetSelectionPosition by remember { mutableStateOf(0f) }
    var selectedFeetValue by remember { mutableStateOf(5) }

    var feetTextHeight: Int by remember {
        mutableStateOf(0)
    }

    var centerY by remember {
        mutableStateOf(0f)
    }
    var YOffset by remember {
        mutableStateOf(0)
    }


    LaunchedEffect(Unit){
        println("selectedFeetValue = $selectedFeetValue")
        println("feetTextHeight = $feetTextHeight")
        feetSelectionPosition = centerY - feetTextHeight * selectedFeetValue
        println("feetSelectionPosition = $feetSelectionPosition")
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
                feetSelectionPosition = it.positionInParent().y
                centerY = it.positionInParent().y
                println("centerY = $centerY")
            }
        )


        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(IntrinsicSize.Max)
                .offset{
                    val minOffsetY = centerY.toInt() - (10 * feetTextHeight )
                    val maxOffsetY = (centerY).toInt()

                    IntOffset(0, (feetSelectionPosition.toInt())
                        .coerceIn( minOffsetY, maxOffsetY))
                }
                .pointerInput(Unit){
                    detectVerticalDragGestures(
                        onDragEnd = {
                            val minOffsetY = centerY.toInt() - (10 * feetTextHeight )
                            val maxOffsetY = (centerY).toInt()

                            YOffset = (feetSelectionPosition).toInt()
                                .coerceIn( minOffsetY, maxOffsetY)

                            // Calculate normalized position (0 to 1)
                            val normalizedPosition = 1f - ((YOffset - minOffsetY).toFloat() / (maxOffsetY - minOffsetY).toFloat())
                            println("normalizedPosition = $normalizedPosition")

                            selectedFeetValue = (normalizedPosition * 10).roundToInt()

                            println("selectedFeetValue = $selectedFeetValue")
                        },
                        onVerticalDrag = { change, dragAmount ->
                            feetSelectionPosition += dragAmount
                        }
                    )
                }
            ,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            (0..10).onEach { feet ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .onGloballyPositioned{
                            feetTextHeight = it.size.height
                        }
                        .border(Dp.Hairline, Color.Black),
                    contentAlignment = androidx.compose.ui.Alignment.Center

                ) {
                    Text(
                        "$feet",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Text("ft",
            modifier = Modifier
                .align(Alignment.CenterEnd)
        )


    }
}

@Preview
@Composable
private fun PreviewHeightInchesComponent() {
    SmartStepTheme {
        HeightInchesComponent(
            modifier = Modifier.fillMaxHeight()

        )

    }
}