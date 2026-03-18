package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.TopSummaryUi

@Composable
fun TopSummaryComponent(
    modifier: Modifier = Modifier,
    uiState: TopSummaryUi
) {

    Column() {
        Text(text = uiState.title.asString())
        Text(text = uiState.value.toString())
    }
}

@Preview
@Composable
private fun PreviewTopSummaryComponent() {
    SmartStepTheme() {
        TopSummaryComponent(
            uiState = TopSummaryUi()
        )

    }
}