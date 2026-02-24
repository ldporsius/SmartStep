package nl.codingwithlinda.smartstep.features.statistics.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import nl.codingwithlinda.smartstep.application.di.DispatcherProvider
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.GramsWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.KG
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.convertWeight
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import nl.codingwithlinda.smartstep.features.statistics.domain.calculations.calculateDistanceCm
import nl.codingwithlinda.smartstep.features.statistics.domain.calculations.caloriesBurned
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.KM
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.MILE
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.cm
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.convertDistance
import nl.codingwithlinda.smartstep.features.statistics.presentation.util.MinuteCounter
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class StatisticsManagerImpl(
    userSettingsRepo: UserSettingsRepo,
    dailyStepRepo: DailyStepRepo,
    walkDurationRepo: WalkDurationRepo,
    dispatcherProvider: DispatcherProvider
): StatisticsManager {

    val userHeightCm = userSettingsRepo.userSettingsObservable.map {
        it.heightCm
    }
    val userWeightKG = userSettingsRepo.userSettingsObservable.map {
        it.weightGrams
    }.map {
        val grams = GramsWeight(it)
        convertWeight(grams, KG)
    }

    val gender = userSettingsRepo.userSettingsObservable.map {
        it.gender
    }

    private val today : DateYYYYMMDD
        get() = DateTimeHelper.toDateYYYYMMDD(System.currentTimeMillis())

    override val stepsToday = dailyStepRepo.stepCountPlusUserOverride.mapNotNull { stepCounts ->
        stepCounts.firstOrNull {
            it.dayEpochDay == today.dateEpochDay
        }?.stepCount
    }

    private val todaysGoal = dailyStepRepo.getDailyStepGoals().mapNotNull { goals ->
        goals.find {
            it.epochDay == today.dateEpochDay
        }
    }

    val currentSystem = userSettingsRepo.unitSystemObservable.map{
        it
    }

    override val distanceWalked = combine(userHeightCm, stepsToday){height, steps ->
        val distance = calculateDistanceCm(height, steps)
        cm(distance)
    }.combine(currentSystem){
            distance, system ->

        when(system){
            UnitSystems.IMPERIAL ->{
                convertDistance(distance, MILE)
            }
            UnitSystems.SI -> {
                convertDistance(distance, KM)
            }
        }
    }.flowOn(dispatcherProvider.default)

    override val caloriesBurned = combine(stepsToday, userWeightKG, gender) { steps, weight, gender ->
        caloriesBurned(steps, weight.weight, gender).roundToInt()
    }

    private val minuteCounter = MinuteCounter()

    override fun startMinuteCounter() {
        minuteCounter.start()
    }

    override fun stopMinuteCounter() {
        minuteCounter.stop()
    }
    override val timeWalked = walkDurationRepo.sessions.filter { sessions ->
        sessions.any{
            it.start.dateYYYYMMDD.dateEpochDay == today.dateEpochDay
        }
    }.combine(minuteCounter.minuteCounter){session, minute ->
        val duration = session.sumOf {
            ( it.end?.timestamp ?: minute ) - it.start.timestamp
        }
        duration.milliseconds.inWholeMinutes.toInt()
    }.flowOn(dispatcherProvider.default)

    override val progressTowardsGoal: Flow<Float> = combine(stepsToday,todaysGoal){
            steps, goal ->
        println("steps: $steps, goal: ${goal.goal}, percentage: ${steps.toFloat() / goal.goal}")

            steps.toFloat() / goal.goal
        }

}