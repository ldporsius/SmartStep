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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.features.ai_integration.data.ai_plugin.AIPluginProvider
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.viewModelFactoryHelper
import nl.codingwithlinda.smartstep.design_system.ui.theme.primary
import nl.codingwithlinda.smartstep.design_system.ui.theme.secondary
import nl.codingwithlinda.smartstep.design_system.ui.theme.white
import nl.codingwithlinda.smartstep.core.data.connectivity.ConnectivityCheck
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.ai.domain.plugin_provider.AIapi
import nl.codingwithlinda.ai.domain.plugin_provider.AImode
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.smartstep.features.ai_integration.features.passive.data.AIUserMessages

@Composable
fun AIMessageComponent(
    aiStateController: AIStateController,
    aiUserMessages: AIUserMessages,
    onMore: () -> Unit,
    modifier: Modifier = Modifier) {

    val context = LocalContext.current


    val aiMessageViewModel = viewModel<AIMessageViewModel>(
        factory = viewModelFactoryHelper {
            AIMessageViewModel(
                aiStateController = aiStateController,
                aiUserMessages = aiUserMessages,
                connectivityMonitor = ConnectivityCheck(context),
                statisticsManager = SmartStepApplication.statisticsManager
            )
        }
    )

    val uiState: AIConnectivityUiState =  aiMessageViewModel.uiState.collectAsStateWithLifecycle().value

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
                            "Try again ⟳",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clickable() {
                                    //todo
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

