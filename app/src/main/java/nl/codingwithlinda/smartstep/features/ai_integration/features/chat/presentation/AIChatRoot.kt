package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.viewModelFactoryHelper
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components.AIChatHistory

@Composable
fun AIChatRoot(
    aiMessenger: AIMessenger,
    onNavBack: () -> Unit
) {

    val chatViewModel = viewModel<AIChatViewModel>(
        factory = viewModelFactoryHelper {
            AIChatViewModel(
                aiMessenger = aiMessenger
            )
        }
    )

    var input by rememberSaveable() {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavBack
            ) {
                Icon(painter = painterResource(R.drawable.chevron),
                    contentDescription = "back",
                    modifier = Modifier.size(24.dp)
                        .rotate(90f)
                )
            }
            Text("AI Coach")
        }
        AIChatHistory(
            messages = chatViewModel.chats.collectAsStateWithLifecycle().value
        )

        Spacer(modifier = Modifier.weight(1f))


        TextField(
            value = input,
            onValueChange = {
                input = it
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        chatViewModel.sendMessage(input)
                        input = ""
                    }
                ) {
                    Icon(painter = painterResource(R.drawable.send),
                        contentDescription = "send"
                    )
                }
            }
        )
    }
}