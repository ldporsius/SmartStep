package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.design_system.ui.theme.primary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white

@Composable
fun UserChatBalloon(
    message: String,
    modifier: Modifier = Modifier
) {
    val boxShape = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 4.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )
    Box(modifier = modifier
        .fillMaxWidth(.75f)
        .background(color = primary, shape = boxShape)
        .padding(24.dp)
    ){
        Text(message,
            color = white
        )
    }
}