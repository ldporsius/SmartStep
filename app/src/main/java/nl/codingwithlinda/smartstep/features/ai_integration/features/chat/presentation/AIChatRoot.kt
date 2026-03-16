package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.codingwithlinda.ai.domain.local_cache.AIChatRepo
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.di.AndroidDispatcherProvider
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.viewModelFactoryHelper
import nl.codingwithlinda.smartstep.core.data.connectivity.ConnectivityCheck
import nl.codingwithlinda.smartstep.core.domain.connectivity.ConnectivityMonitor
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components.AIChatScreen
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.quick_suggestion.QuickSuggestionsController

@Composable
fun AIChatRoot(
    aiStateController: AIStateController,
    userSettingsRepo: UserSettingsRepo,
    onNavBack: () -> Unit
) {

    val context = LocalContext.current
    val connectivityMonitor: ConnectivityMonitor = ConnectivityCheck(context)

    val quickSuggestions = QuickSuggestionsController.getInstance(
        userSettingsRepo = userSettingsRepo,
        statisticsManager = SmartStepApplication.appContainer.statisticsManager,
        aiStateController = aiStateController,
        dispatcherProvider = AndroidDispatcherProvider()
    )

    val chatViewModel = viewModel<AIChatViewModel>(
        factory = viewModelFactoryHelper {
            AIChatViewModel(
                aiStateController = aiStateController,
                quickSuggestionsController = quickSuggestions,
                connectivityMonitor = connectivityMonitor
            )
        }
    )

    AIChatScreen(
        quickSuggestions = quickSuggestions.quickSuggestions,
        isQuickSuggestionsVisible = chatViewModel.isQuickSuggestionsVisible.collectAsStateWithLifecycle().value,
        toggleQuickSuggestions = { chatViewModel.toggleQuickSuggestionsVisibility() },
        history = chatViewModel.chats.collectAsStateWithLifecycle().value,
        uiState = chatViewModel.uiState.collectAsStateWithLifecycle().value,
        onNavBack = onNavBack
    )

}