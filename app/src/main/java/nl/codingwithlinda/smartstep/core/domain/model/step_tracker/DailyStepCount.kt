package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

data class DailyStepCount(
    val dateSeconds: Long,
    val stepCount: Int
)