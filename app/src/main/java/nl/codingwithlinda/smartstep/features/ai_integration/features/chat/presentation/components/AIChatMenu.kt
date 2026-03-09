package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.finite_state.AIChatState
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.finite_state.ToUi
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.quick_suggestion.QuickSuggestion

@Composable
fun AIChatMenu(
    aiChatState: AIChatState,
    quickSuggestions: List<QuickSuggestion> = emptyList(),
    modifier: Modifier = Modifier
) {

    var shouldShowQuickSuggestions by rememberSaveable() {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.clickable(){
                shouldShowQuickSuggestions = !shouldShowQuickSuggestions
            }
                .padding(16.dp)
        ) {
            Text("Quick suggestions",
                style = MaterialTheme.typography.labelLarge)
            Icon(painter = painterResource(R.drawable.chevron),
                contentDescription = null)
        }
        AnimatedVisibility(shouldShowQuickSuggestions) {
            QuickSuggestions(
                suggestions = quickSuggestions,
                isChatEnabled = aiChatState is AIChatState.Online
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        aiChatState.ToUi(
            modifier = modifier
        )

    }
}

@Preview
@Composable
private fun PreviewAIChatMenu() {
    SmartStepTheme() {
        AIChatMenu(
            aiChatState = AIChatState.Online(
                message = "",
                onAction = {}
            ),
        )

    }
}