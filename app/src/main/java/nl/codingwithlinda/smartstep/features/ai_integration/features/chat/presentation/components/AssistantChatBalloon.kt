package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design_system.ui.theme.white

@Composable
fun AssistantChatBalloon(
    modifier: Modifier = Modifier,
    message: String
) {
    val boxShape = RoundedCornerShape(
        topStart = 4.dp,
        topEnd = 40.dp,
        bottomStart = 40.dp,
        bottomEnd = 40.dp
    )
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
    ) {
        Image(
            painter = painterResource(R.drawable.robokop),
            contentDescription = null
        )

        Box(
            modifier = modifier
                .background(color = white, shape = boxShape)
                .border(width = 1.dp, color = white, shape = boxShape)
                .padding(24.dp)
        ) {
            Text(message)
        }
    }

}