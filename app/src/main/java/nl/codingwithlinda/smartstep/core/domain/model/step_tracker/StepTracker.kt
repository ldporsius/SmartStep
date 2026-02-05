package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import kotlinx.coroutines.flow.Flow

interface StepTracker {
    fun track()
    val stepsPerDay: Flow<Int>
}