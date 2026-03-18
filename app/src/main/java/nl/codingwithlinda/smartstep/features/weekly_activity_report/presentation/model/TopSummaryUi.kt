package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model

import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.smartstep.R

data class TopSummaryUi(
    val title: UiText = UiText.StringResourceText(R.string.steps),
    val value: Int = 100,
    val subtitle: UiText = UiText.StringResourceText(R.string.daily_average, 191),
    val timeSpan: UiText = UiText.StringResourceText(R.string.this_week)
)
