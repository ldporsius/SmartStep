package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.layout.positionInRoot
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


    var listValuesYOffset by remember { mutableStateOf(0f) }
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

    LaunchedEffect(valueTextHeight){

        listValuesYOffset = centerY - valueTextHeight.toFloat() * indexInList
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
                    IntOffset(0, (listValuesYOffset.toInt())
                        .coerceIn( minOffsetY, maxOffsetY))
                }
                .pointerInput(Unit){
                    detectVerticalDragGestures(
                        onDragEnd = {

                            YOffset = (listValuesYOffset).toInt()


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
                            listValuesYOffset += dragAmount

                        }
                    )
                }
            ,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            values.onEach { value ->


                var boxPosition by remember { mutableStateOf(0f) }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .onGloballyPositioned{
                           boxPosition = it.positionInRoot().y
                        }
                        ,
                    contentAlignment = androidx.compose.ui.Alignment.Center

                ) {
                    val visibilityRange = IntRange((centerY - 4 * valueTextHeight).toInt(),
                        (centerY + 5 * valueTextHeight).toInt()
                    )
                    val visible = boxPosition.roundToInt() in visibilityRange

                    val visibleAnimation = animateFloatAsState(
                        targetValue = if ( visible) 1f else 0f,
                        animationSpec = androidx.compose.animation.core.tween(300)
                    )
                    Text(
                        "$value",
                        textAlign = TextAlign.Center,
                        color = Color.Black.copy(alpha = visibleAnimation.value)
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