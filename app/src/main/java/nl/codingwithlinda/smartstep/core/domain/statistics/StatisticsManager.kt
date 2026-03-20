package nl.codingwithlinda.smartstep.core.domain.statistics

import kotlinx.coroutines.flow.Flow
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.unit_conversion.data.distance.ConcreteDistance

interface StatisticsManager {

    val stepsToday: Flow<Int>
    val todaysGoal: Flow<Int>
    val distanceWalked: Flow<ConcreteDistance>
    val caloriesBurned: Flow<Int>
    val timeWalked: Flow<Int>

    val progressTowardsGoal: Flow<Float>

    val trend: Flow<Map<DateYYYYMMDD, Float>>

}