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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.bg
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.finite_state.AIChatState
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.quick_suggestion.QuickSuggestion
import nl.codingwithlinda.ai_firebase.tests.fakeChatHistory
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.form_factor.FormFactorWrapper

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
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
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
                    painter = painterResource(R.drawable.arrow),
                    contentDescription = "back",
                    modifier = Modifier
                        .size(24.dp)

                )
            }
            Text(
                "AI Coach",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.size(48.dp))
        }
        HorizontalDivider()

        Box(modifier = Modifier.weight(1f)) {

            FormFactorWrapper {
                AIChatHistory(
                    messages = history
                )
            }
        }
        HorizontalDivider()

        Surface(
            modifier = Modifier.fillMaxWidth()
            ,
            color = white
        ) {
            FormFactorWrapper {
                AIChatMenu(
                    aiChatState = uiState,
                    quickSuggestions = quickSuggestions,
                    modifier = Modifier
                        .width(400.dp)
                        .padding(bottom = 16.dp),
                )
            }

        }
    }
}

@PreviewScreenSizes
@Composable
private fun PreviewAIChatScreen() {
    SmartStepTheme() {
        AIChatScreen(
            quickSuggestions = emptyList(),
            history = fakeChatHistory(),
            uiState = AIChatState.Online(
                message = "",
                onAction = {}
            ),
            onNavBack = {}
        )

    }
}