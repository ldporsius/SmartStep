package nl.codingwithlinda.smartstep.features.statistics.domain

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.unit_conversion.data.ConcreteDistance

interface StatisticsManager {

    val stepsToday: Flow<Int>
    val todaysGoal: Flow<Int>
    val distanceWalked: Flow<ConcreteDistance>
    val caloriesBurned: Flow<Int>
    val timeWalked: Flow<Int>

    val progressTowardsGoal: Flow<Float>

    val trend: Flow<Map<DateYYYYMMDD, Float>>


    fun startMinuteCounter()
    fun stopMinuteCounter()
}