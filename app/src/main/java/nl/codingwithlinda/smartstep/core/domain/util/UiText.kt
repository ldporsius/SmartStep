package nl.codingwithlinda.smartstep.core.domain.util

sealed interface UiText {
    data class DynamicText(val text: String): UiText
    data class StringResourceText(val resId: Int, val args: List<*>): UiText
}