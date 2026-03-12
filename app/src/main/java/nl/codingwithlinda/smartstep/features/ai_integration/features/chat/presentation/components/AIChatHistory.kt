package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin

@Composable
fun AIChatHistory(
    messages: List<AIMessage>
) {

    @Composable
    fun BoxScope.horizontalAlign(origin: AIMessageOrigin): Modifier {
        val align = when (origin) {
            AIMessageOrigin.USER -> Alignment.CenterEnd
            AIMessageOrigin.ASSISTANT -> Alignment.TopStart
        }
        return Modifier.align(align)
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(messages){aiMsg ->

            Box(modifier = Modifier.fillMaxWidth()) {
                val alignModifier = this.horizontalAlign(aiMsg.origin)
                when (aiMsg.origin) {
                    AIMessageOrigin.USER -> {
                        UserChatBalloon(
                            message = aiMsg.message,
                            modifier = alignModifier
                        )
                    }

                    AIMessageOrigin.ASSISTANT -> {
                        AssistantChatBalloon(
                            message = aiMsg.message
                        )
                    }
                }
            }
        }
    }
}