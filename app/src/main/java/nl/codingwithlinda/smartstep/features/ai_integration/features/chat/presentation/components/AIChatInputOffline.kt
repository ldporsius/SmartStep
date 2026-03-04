package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R

@Composable
fun AIChatInputOffline(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            enabled = false,
            placeholder = {
                Text("You are offline")
            }
        )

        Box(modifier = Modifier
            .clip(CircleShape)
        ){
            Image(
                painter = painterResource(R.drawable.send),
                contentDescription = "You are offline",
                alpha = 0.5f,
                colorFilter = ColorFilter.tint(color = Color.LightGray, blendMode = BlendMode.Hue)
            )
        }
    }
}