package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.di.AndroidDispatcherProvider
import nl.codingwithlinda.smartstep.application.di.ai_plugin.AIPluginProvider
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.viewModelFactoryHelper
import nl.codingwithlinda.smartstep.core.domain.connectivity.ConnectivityMonitor
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.data.connectivity.ConnectivityCheck
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.chat.Gemini_2_5_Chat_Config
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.chat.GeminiChatMessengerImpl
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components.AIChatScreen
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.quick_suggestion.QuickSuggestionsController

@Composable
fun AIChatRoot(
    mode: AIPluginProvider.Companion.AImode,
    api: AIPluginProvider.Companion.AIapi,
    userSettingsRepo: UserSettingsRepo,
    aiSessionRepo: AISessionRepo,
    onNavBack: () -> Unit
) {

    val aiStateController = remember {
        AIPluginProvider(
            aiSessionRepo = aiSessionRepo
        ).getAIStateController(
            mode = mode,
            api = api
        )
    }


    val context = LocalContext.current
    val connectivityMonitor: ConnectivityMonitor = ConnectivityCheck(context)

    val chatViewModel = viewModel<AIChatViewModel>(
        factory = viewModelFactoryHelper {
            AIChatViewModel(
                aiStateController = aiStateController,
                aiSessionRepo = aiSessionRepo,
                connectivityMonitor = connectivityMonitor
            )
        }
    )


    val quickSuggestions = QuickSuggestionsController(
        userSettingsRepo = userSettingsRepo,
        statisticsManager = SmartStepApplication.statisticsManager,
        aiStateController = aiStateController,
        dispatcherProvider = AndroidDispatcherProvider()
    )


    AIChatScreen(
        quickSuggestions = quickSuggestions.quickSuggestions,
        history = chatViewModel.chats.collectAsStateWithLifecycle().value,
        uiState = chatViewModel.uiState.collectAsStateWithLifecycle().value,
        onNavBack = onNavBack
    )


}