package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin

@Composable
fun AIChatScreen(
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
    LazyColumn() {
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