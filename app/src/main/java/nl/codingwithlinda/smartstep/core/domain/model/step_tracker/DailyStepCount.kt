package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

data class DailyStepCount(
    val dayEpochSeconds: Long,
    val stepCount: Int
)