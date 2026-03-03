package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.components

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.core.presentation.util.asString
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.design_system.ui.theme.bg
import nl.codingwithlinda.smartstep.design_system.ui.theme.secondary
import nl.codingwithlinda.smartstep.design_system.ui.theme.textPrimary
import nl.codingwithlinda.smartstep.design_system.ui.theme.textSecondary
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.quick_suggestion.QuickSuggestion
import nl.codingwithlinda.smartstep.tests.ai_integration.fakeQuickSuggestionsController

@Composable
fun QuickSuggestions(
    suggestions: List<QuickSuggestion>,
    modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        suggestions.forEach {qs ->
            QuickSuggestionButton(
                text = qs.title.asString()
            ) {
                qs.onAction()
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
            modifier = Modifier.fillMaxWidth()
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

@Preview
@Composable
private fun PreviewQuickSuggestions() {
    SmartStepTheme() {
        QuickSuggestions(
            suggestions = fakeQuickSuggestionsController.quickSuggestions
        )
    }
}