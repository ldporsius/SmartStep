package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.quick_suggestion

import nl.codingwithlinda.smartstep.core.domain.util.UiText

data class QuickSuggestion(
    val title: UiText,
    val onAction: () -> Unit
)
