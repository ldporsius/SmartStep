package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatAction

@Composable
fun AIChatInput(
    message: String = "",
    onAction: (AIChatAction) -> Unit,
    quickSuggestions: @Composable () -> Unit,
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
            quickSuggestions()
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = {
                    onAction(AIChatAction.ChatInput(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Box(modifier = Modifier

                .clickable {
                    onAction(AIChatAction.SendMessage(message))
                }
            ){
                Image(
                    painter = painterResource(R.drawable.send),
                    contentDescription = "send"
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAIChatInput() {
    SmartStepTheme() {
        AIChatInput(
            message = "",
            onAction = {},
            quickSuggestions = { QuickSuggestions(
                suggestions = emptyList()
            ) }
        )

    }
}