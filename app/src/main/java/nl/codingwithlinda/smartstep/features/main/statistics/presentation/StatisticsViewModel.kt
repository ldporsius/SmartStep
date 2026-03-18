package nl.codingwithlinda.smartstep.features.main.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.presentation.util.UiText
import nl.codingwithlinda.smartstep.core.domain.statistics.StatisticsManager
import nl.codingwithlinda.smartstep.features.main.statistics.presentation.model.StatisticsUi
import nl.codingwithlinda.smartstep.features.main.statistics.presentation.util.toUi
import java.util.Locale

class StatisticsViewModel(
    statisticsManager: StatisticsManager,
): ViewModel() {

    private val _statistics = MutableStateFlow<StatisticsUi>(
        StatisticsUi(
            distance = UiText.DynamicText("0"),
            energy = UiText.DynamicText("0"),
            time = UiText.DynamicText("0")
        )
    )

    val statistics = _statistics.asStateFlow()


    init {
        viewModelScope.launch {
            statisticsManager.distanceWalked.collect {concreteDistance ->
                val formatted = String.format(Locale.getDefault(),"%.1f", concreteDistance.value)

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
            statisticsManager.caloriesBurned.collect {
                val formatted = String.format(Locale.getDefault(),"%d", it)

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
            statisticsManager.timeWalked.collect {duration ->
                _statistics.update {
                    it.copy(
                        time = UiText.DynamicText(
                            "${duration} min"
                        )
                    )
                }
            }
        }

        statisticsManager.startMinuteCounter()

    }
}
