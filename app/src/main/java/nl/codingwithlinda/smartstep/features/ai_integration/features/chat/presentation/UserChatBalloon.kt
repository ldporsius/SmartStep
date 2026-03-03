package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.codingwithlinda.smartstep.design_system.ui.theme.primary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white

@Composable
fun UserChatBalloon(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxWidth(.75f)
        .background(color = primary)
    ){
        Text(message,
            color = white
        )
    }
}