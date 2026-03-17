package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction

import nl.codingwithlinda.smartstep.features.weekly_activity_report.domain.ReportTarget
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.util.ReportTargetUi

data class ReportTargetUiState(
    val selectedTarget: ReportTarget = ReportTarget.STEPS,
)
