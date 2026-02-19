package nl.codingwithlinda.smartstep.features.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.di.DispatcherProvider
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.GramsWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.KG
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.convertWeight
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.features.statistics.domain.calculations.calculateDistanceCm
import nl.codingwithlinda.smartstep.features.statistics.domain.calculations.caloriesBurned
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.KM
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.MILE
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.cm
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.convertDistance
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi
import nl.codingwithlinda.smartstep.features.statistics.presentation.util.MinuteCounter
import nl.codingwithlinda.smartstep.features.statistics.presentation.util.MinuteCounter.minuteCounter
import nl.codingwithlinda.smartstep.features.statistics.presentation.util.toUi
import nl.codingwithlinda.smartstep.tests.fakeStatistics
import kotlin.time.Duration.Companion.milliseconds

class StatisticsViewModel(
    userSettingsRepo: UserSettingsRepo,
    dailyStepRepo: DailyStepRepo,
    walkDurationRepo: WalkDurationRepo,
    dispatcherProvider: DispatcherProvider
): ViewModel() {

    private val _statistics = MutableStateFlow<StatisticsUi>(
        fakeStatistics
    )

    val statistics = _statistics.asStateFlow()

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
    val stepsTaken = dailyStepRepo.stepCount.map {
        it.stepCount
    }

    val currentSystem = userSettingsRepo.unitSystemObservable.map{
        it
    }

    val distanceWalked = combine(userHeightCm, stepsTaken){height, steps ->
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


    val caloriesBurned = combine(stepsTaken, userWeightKG, gender) { steps, weight, gender ->
        caloriesBurned(steps, weight.weight, gender)
    }



    val timeWalked = walkDurationRepo.sessions.filter { sessions ->
        val today = DailyStepCountCreator.getTodayAsSeconds()
        sessions.any{
            it.start.dateSeconds == today
        }
    }.combine(minuteCounter){session, minute ->
        val duration = session.sumOf {
            ( it.end?.timestamp ?: minute ) - it.start.timestamp
        }
        duration
    }.flowOn(dispatcherProvider.default)

    init {
        viewModelScope.launch {
            distanceWalked.collect {concreteDistance ->
                val formatted = String.format(java.util.Locale.getDefault(),"%.1f", concreteDistance.value)

                _statistics.update {
                    it.copy(
                        distance = UiText.DynamicText(
                            formatted + " " + concreteDistance.distance.toUi()
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            caloriesBurned.collect {
                val formatted = String.format(java.util.Locale.getDefault(),"%.0f", it)

                _statistics.update {
                    it.copy(
                        energy = UiText.DynamicText(
                            "$formatted kcal"
                        )
                    )
                }
            }
        }
        viewModelScope.launch {
            timeWalked.collect {duration ->
                _statistics.update {
                    it.copy(
                        time = UiText.DynamicText(
                            "${duration.milliseconds.inWholeMinutes} min"
                        )
                    )
                }
            }
        }
        MinuteCounter.start()
    }
}
