package nl.codingwithlinda.smartstep.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import nl.codingwithlinda.smartstep.core.domain.util.UiText

@Composable
fun UiText.asString(): String{
    val context = LocalContext.current
    return when(this){
        is UiText.DynamicText -> this.text
        is UiText.StringResourceText -> context.getString(this.resId, this.args)
    }
}