package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import nl.codingwithlinda.smartstep.features.ai_integration.presentation.AIConnectivityUiState

@Composable
fun AIMessageComponent(
    uiState: AIConnectivityUiState,
    onMore: () -> Unit,
    modifier: Modifier = Modifier) {

    @Composable
    fun AIIcon(){
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
    }
    ElevatedCard(modifier = modifier,
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = white
        )
    ) {

        AnimatedContent(uiState) { state ->
            when (state) {

                is AIConnectivityUiState.OffLine -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            "Try again ",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clickable() {
                                    onMore()
                                },
                            style = MaterialTheme.typography.labelLarge
                        )

                        Column() {
                            AIIcon()
                            Text(
                                state.message.message,
                                modifier = Modifier.padding(
                                    24.dp
                                )
                            )
                        }
                    }
                }

                is AIConnectivityUiState.OnLine -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            "More    >",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clickable() {
                                    onMore()
                                },
                            style = MaterialTheme.typography.labelLarge
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),

                        ) {
                            AIIcon()
                            if (state.message != null) {
                                Text(
                                    state.message.message,
                                    modifier = Modifier.padding(
                                        24.dp
                                    )
                                )
                            } else {
                                LinearProgressIndicator(
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAIMessageComponent() {
    SmartStepTheme() {
        AIMessageComponent(
            uiState = AIConnectivityUiState.OnLine(
                message = AIMessage(
                    "hello",
                    AIMessageOrigin.ASSISTANT
                )
            ),
            onMore = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}