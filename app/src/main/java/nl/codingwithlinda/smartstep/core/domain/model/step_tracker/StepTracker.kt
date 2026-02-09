package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import kotlinx.coroutines.flow.Flow

interface StepTracker {
    fun initialize()
    fun start()
    fun stop()
    val stepsTaken: Flow<Int>
}