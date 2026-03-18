package nl.codingwithlinda.smartstep.core.presentation.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class DynamicText(val text: String): UiText
    class StringResourceText(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicText -> text
            is StringResourceText -> {
                // The spread operator (*) must be used to pass the args array as individual arguments to stringResource.
                // We also map nulls to empty strings to satisfy the expected Any type.
                val formatArgs = args.map { it }.toTypedArray()
                stringResource(resId, *formatArgs)
            }
        }
    }
}