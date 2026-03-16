package nl.codingwithlinda.smartstep.features.statistics.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.stepGoalRange
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import nl.codingwithlinda.smartstep.features.statistics.domain.calculations.calculateDistanceCm
import nl.codingwithlinda.smartstep.features.statistics.domain.calculations.caloriesBurned
import nl.codingwithlinda.smartstep.features.statistics.presentation.util.MinuteCounter
import nl.codingwithlinda.unit_conversion.data.distance.ConcreteDistance
import nl.codingwithlinda.unit_conversion.data.distance.DistanceConverter
import nl.codingwithlinda.unit_conversion.data.weight.GramsWeight
import nl.codingwithlinda.unit_conversion.data.weight.WeightUnitConverter
import nl.codingwithlinda.unit_conversion.domain.UnitSystems
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class StatisticsManagerImpl(
    userSettingsRepo: UserSettingsRepo,
    dailyStepRepo: DailyStepRepo,
    walkDurationRepo: WalkDurationRepo,
    dispatcherProvider: DispatcherProvider,
    applicationScope: CoroutineScope
): StatisticsManager {

    val userHeightCm = userSettingsRepo.userSettingsObservable.map {
        it.heightCm
    }
    val userWeightKG = userSettingsRepo.userSettingsObservable.map {
        it.weightGrams
    }.map {
        val grams = GramsWeight(it)
        WeightUnitConverter.toKg(grams)
    }

    val gender = userSettingsRepo.userSettingsObservable.map {
        it.gender
    }

    private val today : DateYYYYMMDD
        get() = DateTimeHelper.toDateYYYYMMDD(System.currentTimeMillis())

    override val stepsToday = dailyStepRepo.stepCountPlusUserOverride.map{ stepCounts ->
        stepCounts.firstOrNull {
            it.dayEpochDay == today.dateEpochDay
        }?.stepCount ?: 0
    }.shareIn(
        applicationScope,
        SharingStarted.Eagerly,
        replay = 10
    )

    override val todaysGoal = dailyStepRepo.getDailyStepGoals().mapNotNull { goals ->
        goals.find {
            it.epochDay == today.dateEpochDay
        }?.goal
    }.shareIn(
        applicationScope,
        SharingStarted.Eagerly,
        replay = 10
    )

    override val progressTowardsGoal: Flow<Float> = combine(stepsToday,todaysGoal){
            steps, goal ->
        steps.toFloat() / goal
    }

    val currentSystem = userSettingsRepo.unitSystemObservable.map{
        it
    }

    override val distanceWalked = combine(userHeightCm, stepsToday){height, steps ->
        val distance = calculateDistanceCm(height, steps)
        ConcreteDistance.cm(distance)
    }.combine(currentSystem){ distance, system ->
        when(system){
            UnitSystems.IMPERIAL ->{
                DistanceConverter.toMile(distance)
            }
            UnitSystems.SI -> {
                DistanceConverter.toKm(distance)
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



    override val trend = dailyStepRepo.stepCountPlusUserOverride
        .map { items ->
          val percentage =  items.map {count ->
                val goal = dailyStepRepo.getGoalForDay(count.dateYYYYMMDD)?.goal ?: stepGoalRange.first()
                val percent = count.stepCount.toFloat() / goal

              count.dateYYYYMMDD to percent
            }.toMap()

           percentage
        }

}