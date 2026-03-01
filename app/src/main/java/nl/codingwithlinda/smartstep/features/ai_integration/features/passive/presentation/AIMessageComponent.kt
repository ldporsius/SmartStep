package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.primary
import nl.codingwithlinda.smartstep.design_system.ui.theme.secondary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin

@Composable
fun AIMessageComponent(
    message: AIMessage,
    onMore: () -> Unit,
    modifier: Modifier = Modifier) {

    ElevatedCard(modifier = modifier,
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = white
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = secondary)
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ai_artificial_intelligence),
                    contentDescription = "AI logo",
                    tint = primary
                )

            }
            Text(
                "More    >",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable(){
                        onMore()
                    }
                ,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Text(message.message,
            modifier = Modifier.padding(
                24.dp
            ))
    }
}

@Preview
@Composable
private fun PreviewAIMessageComponent() {
    SmartStepTheme() {
        AIMessageComponent(
            message = AIMessage(
                "hello",
                AIMessageOrigin.ASSISTANT
            ),
            onMore = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}