package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.quick_suggestion

import nl.codingwithlinda.core.domain.util.UiText

data class QuickSuggestion(
    val title: UiText,
    val onAction: () -> Unit
)
