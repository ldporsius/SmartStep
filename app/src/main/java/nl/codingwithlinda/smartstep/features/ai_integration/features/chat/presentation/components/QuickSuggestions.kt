package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design_system.ui.theme.bg
import nl.codingwithlinda.smartstep.design_system.ui.theme.textPrimary
import nl.codingwithlinda.smartstep.design_system.ui.theme.textSecondary
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.quick_suggestion.QuickSuggestion

@Composable
fun QuickSuggestions(
    isChatEnabled: Boolean,
    suggestions: List<QuickSuggestion>,
   ) {

    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        suggestions.forEach {qs ->
            QuickSuggestionButton(
                text = qs.title.asString()
            ) {
                if (isChatEnabled){
                    qs.onAction()
                }
                else{
                    Toast.makeText(context, "You are offline", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
}

@Composable
fun QuickSuggestionButton(
    text: String,
    onClick: () -> Unit
) {
    val buttonShape = RoundedCornerShape(16.dp)
    Surface(
        shape = buttonShape,
        color = bg,
        contentColor = textPrimary
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable() {
                    onClick()
                }
                .border(width = 1.dp, color = textSecondary, shape = buttonShape)
                .padding(vertical = 16.dp, horizontal = 16.dp)

        ) {
            Text(text,
                style = MaterialTheme.typography.labelLarge)
        }
    }
}
