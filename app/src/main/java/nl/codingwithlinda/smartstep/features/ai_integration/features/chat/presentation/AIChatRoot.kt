package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.di.AndroidDispatcherProvider
import nl.codingwithlinda.smartstep.application.di.viewmodel_service.viewModelFactoryHelper
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.ai_integration.data.GeminiAIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.data.Gemini_2_5_Chat_Config
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components.AIChatScreen
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.quick_suggestion.QuickSuggestionsController
import nl.codingwithlinda.smartstep.tests.ai_integration.FakeAIMessenger

@Composable
fun AIChatRoot(
    userSettingsRepo: UserSettingsRepo,
    onNavBack: () -> Unit
) {

   /* val geminiMessenger = GeminiAIMessenger(
        geminiGonfig = Gemini_2_5_Chat_Config()
    )*/
    val geminiMessenger = FakeAIMessenger()
    val chatViewModel = viewModel<AIChatViewModel>(
        factory = viewModelFactoryHelper {
            AIChatViewModel(
                aiMessenger = geminiMessenger
            )
        }
    )


    val quickSuggestions = QuickSuggestionsController(
        userSettingsRepo = userSettingsRepo,
        statisticsManager = SmartStepApplication.statisticsManager,
        aiMessenger = geminiMessenger,
        dispatcherProvider = AndroidDispatcherProvider()
    )


    AIChatScreen(
        quickSuggestions = quickSuggestions.quickSuggestions,
        history = chatViewModel.chats.collectAsStateWithLifecycle().value,
        uiState = chatViewModel.uiState.collectAsStateWithLifecycle().value,
        onAction = chatViewModel::onAction,
        onNavBack = onNavBack
    )


}