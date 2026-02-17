package nl.codingwithlinda.smartstep.features.statistics.presentation

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.userSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.WeightUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.Weights
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.features.statistics.domain.calculations.calculateDistanceCm
import nl.codingwithlinda.smartstep.features.statistics.domain.calculations.caloriesBurned
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.KM
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.MILE
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.cm
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.convertDistance
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi
import nl.codingwithlinda.smartstep.features.statistics.presentation.util.toUi
import nl.codingwithlinda.smartstep.tests.fakeStatistics
import kotlin.math.roundToInt

class StatisticsViewModel(
    private val userSettingsRepo: UserSettingsRepo,
    private val dailyStepRepo: DailyStepRepo
): ViewModel() {

    val statistics = MutableStateFlow<StatisticsUi>(
        fakeStatistics
    )

    val userHeightCm = userSettingsRepo.userSettingsObservable.map {
        it.heightCm
    }
    val userWeightKG = userSettingsRepo.userSettingsObservable.map {
        it.weightGrams
    }.map {
        WeightUnits.Grams(it.roundToInt()).convert<WeightUnits.KG>(Weights.KG)
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
    }

    val caloriesBurned = combine(stepsTaken, userWeightKG, gender) { steps, weight, gender ->
        caloriesBurned(steps, weight.kg.toDouble(), gender)
    }

    init {
        viewModelScope.launch {
            distanceWalked.collect {concreteDistance ->
                val formatted = String.format(java.util.Locale.getDefault(),"%.1f", concreteDistance.value)

                statistics.update {
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

                statistics.update {
                    it.copy(
                        energy = UiText.DynamicText(
                            "$formatted kcal"
                        )
                    )
                }
            }
        }

    }
}
