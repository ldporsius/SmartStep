package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.form_factor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormFactorWrapper(
    content: @Composable () -> Unit
   ) {

    Box(modifier = Modifier
        .width(600.dp),
        contentAlignment = Alignment.TopCenter
    ){
        content()
    }
}