package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model

import nl.codingwithlinda.core.domain.util.UiText

data class TopSummaryUi(
    val title: String ="Steps",
    val value: Int = 100,
    val subtitle: UiText = UiText.DynamicText("Daily average: 191"),
    val timeSpan: String = "this week"
)
