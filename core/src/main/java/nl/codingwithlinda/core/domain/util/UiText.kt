package nl.codingwithlinda.core.domain.util

sealed interface UiText {
    data class DynamicText(val text: String): UiText
    class StringResourceText(val resId: Int, vararg val args: Any): UiText
}