package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction

import nl.codingwithlinda.smartstep.features.weekly_activity_report.domain.ReportTarget

sealed interface ReportTargetAction {
    data class SetTargetAction(val target: ReportTarget): ReportTargetAction
}