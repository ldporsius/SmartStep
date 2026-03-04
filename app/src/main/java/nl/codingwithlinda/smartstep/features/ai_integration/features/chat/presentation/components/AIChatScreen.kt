package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.bg
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatAction
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.finite_state.AIChatState
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.quick_suggestion.QuickSuggestion
import nl.codingwithlinda.smartstep.tests.ai_integration.fakeChatHistory

@Composable
fun AIChatScreen(
    quickSuggestions: List<QuickSuggestion>,
    history: List<AIMessage>,
    uiState: AIChatState,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = bg)
            .safeContentPadding()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onNavBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron),
                    contentDescription = "back",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(90f)
                )
            }
            Text("AI Coach")
            Spacer(modifier = Modifier.size(1.dp))
        }
        Box(modifier = Modifier.weight(1f)){
        AIChatHistory(
            messages = history
        )
    }

        HorizontalDivider()

        Surface(
            modifier = Modifier,
            color = white
        ) {
            AIChatMenu(
                aiChatState = uiState,
                quickSuggestions = quickSuggestions,
                modifier = Modifier.padding(16.dp),
            )
        }

    }
}

@Preview
@Composable
private fun PreviewAIChatScreen() {
    SmartStepTheme() {
        AIChatScreen(
            quickSuggestions = emptyList(),
            history = fakeChatHistory(),
            uiState = AIChatState.Offline,
            onNavBack = {}
        )

    }
}