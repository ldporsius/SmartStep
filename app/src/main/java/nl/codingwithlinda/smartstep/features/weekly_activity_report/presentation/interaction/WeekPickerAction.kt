package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction

sealed interface WeekPickerAction {
    object PreviousWeek : WeekPickerAction
    object NextWeek : WeekPickerAction
}
