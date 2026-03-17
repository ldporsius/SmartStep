package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model

data class TopSummaryUi(
    val title: String ="Steps",
    val value: Int = 100,
    val subtitle: String =  "Daily average: 191",
    val timeSpan: String = "this week"
)
