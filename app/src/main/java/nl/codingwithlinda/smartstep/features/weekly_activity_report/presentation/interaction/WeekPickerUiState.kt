package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction

data class WeekPickerUiState(
    val isPreviousEnabled: Boolean = false,
    val isNextEnabled: Boolean = false,
    val weekRange: String = ""
)
