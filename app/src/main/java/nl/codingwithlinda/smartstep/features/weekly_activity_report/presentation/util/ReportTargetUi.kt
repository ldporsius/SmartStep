package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.util

import androidx.annotation.DrawableRes
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.features.weekly_activity_report.domain.ReportTarget


data class ReportTargetUi(
    @param:DrawableRes
    val icon:  Int,
    val text: String
)

fun ReportTarget.toUi() = when(this){
    ReportTarget.STEPS -> {
        ReportTargetUi(
            icon = R.drawable.sneakers,
            text = "Steps"
        )
    }
    ReportTarget.CALORIES -> {
        ReportTargetUi(
            icon = R.drawable.weight_diet,
            text = "Calories"
        )
    }
    ReportTarget.TIME -> {
        ReportTargetUi(
            icon = R.drawable.time_clock,
            text = "Time"
        )
    }
    ReportTarget.DISTANCE -> {
        ReportTargetUi(
            icon = R.drawable.location_track,
            text = "Distance"
        )
    }
}