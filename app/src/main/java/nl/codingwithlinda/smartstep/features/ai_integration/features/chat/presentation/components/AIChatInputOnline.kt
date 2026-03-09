package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatAction

@Composable
fun AIChatInputOnline(
    modifier: Modifier = Modifier,
    onAction: (AIChatAction) -> Unit,
    message: String
) {

    val softKeyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = {
                onAction(AIChatAction.ChatInput(it))
            },
            modifier = Modifier
                .weight(1f)
        )

        Box(modifier = Modifier
            .clickable {
                onAction(AIChatAction.SendMessage(message))
                softKeyboardController?.hide()
            }
        ){
            Image(
                painter = painterResource(R.drawable.send),
                contentDescription = "send"
            )
        }
    }
}