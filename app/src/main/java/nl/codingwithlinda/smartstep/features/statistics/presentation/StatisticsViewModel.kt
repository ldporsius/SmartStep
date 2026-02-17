package nl.codingwithlinda.smartstep.features.statistics.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi
import nl.codingwithlinda.smartstep.tests.fakeStatistics

class StatisticsViewModel: ViewModel() {

    val statistics = MutableStateFlow<StatisticsUi>(
        fakeStatistics
    )

}