package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.quick_suggestion

import nl.codingwithlinda.smartstep.core.presentation.util.UiText

data class QuickSuggestion(
    val title: UiText,
    val onAction: () -> Unit
)
