package nl.codingwithlinda.smartstep.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import kotlin.math.roundToInt

@Composable
fun CustomBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    var childTop by remember { mutableIntStateOf(0) }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black.copy(alpha = 0.5f))
        .pointerInput(
            Unit
        ){
            detectTapGestures {
                println("--- CUSTOM BOTTOM SHEET --- tap gesture on ${it.y}, child top is $childTop")
                if( it.y < childTop){
                    onDismiss()
                }
            }
        }

    ){
        Box(
            modifier = Modifier
                .background(color = white, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .align(Alignment.BottomCenter)
                .onGloballyPositioned {
                        childTop = it.localToScreen(Offset.Zero).y.roundToInt()
                    }
                ,
        ) {
            content()
        }

    }
}