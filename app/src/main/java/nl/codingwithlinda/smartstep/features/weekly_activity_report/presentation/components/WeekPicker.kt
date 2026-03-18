package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.WeekPickerAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.WeekPickerUiState

@Composable
fun WeekPicker(
    modifier: Modifier = Modifier,
    uiState: WeekPickerUiState,
    onAction: (WeekPickerAction) -> Unit = {}
) {

    @Composable
    fun iconColor(enabled: Boolean) = if(enabled) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.secondary

    @Composable
    fun iconModifier(enabled: Boolean) = Modifier
        .size(48.dp)
        .background(iconColor(enabled), CircleShape)

    Row(modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {onAction(WeekPickerAction.PreviousWeek)},
            modifier = iconModifier(enabled = uiState.isPreviousEnabled),
            enabled = uiState.isPreviousEnabled,

        ) {
            Icon(painter = painterResource(R.drawable.arrow_left),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null)
        }

        Text(uiState.weekRange)

        IconButton(
            onClick = {onAction(WeekPickerAction.NextWeek) },
            modifier = iconModifier(enabled = uiState.isNextEnabled),
            enabled = uiState.isNextEnabled
        ) {
            Icon(painter = painterResource(R.drawable.arrow_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWeekPicker() {
    SmartStepTheme() {
        WeekPicker(
            modifier = Modifier.width(480.dp),
            uiState = WeekPickerUiState(
                isPreviousEnabled = true,
                isNextEnabled = false,
                weekRange = "Nov 16 - Nov 22"
            )
        )
    }
}