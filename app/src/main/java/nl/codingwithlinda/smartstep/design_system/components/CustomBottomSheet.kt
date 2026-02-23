package nl.codingwithlinda.smartstep.design_system.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned

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
                if( it.y < childTop){
                    onDismiss()
                }
            }
        }

    ){
        AnimatedVisibility(
            visible = true,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned(
                    onGloballyPositioned = {
                        childTop = it.size.height
                    }
                ),
            enter = slideInVertically(
                animationSpec = tween(1500)
            ) {
                -it / 2
            },
            exit = slideOutVertically {
                it
            }
        ) {
            content()
        }

    }
}