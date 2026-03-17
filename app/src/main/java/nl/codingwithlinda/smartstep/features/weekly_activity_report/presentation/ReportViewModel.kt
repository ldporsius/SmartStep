package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.features.weekly_activity_report.data.WeeklyStatisticsManager
import nl.codingwithlinda.smartstep.features.weekly_activity_report.domain.ReportTarget
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetUiState
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.TopSummaryUi
import kotlin.math.roundToInt

class ReportViewModel(
    private val weeklyStatisticsManager: WeeklyStatisticsManager
): ViewModel() {


    private val _uiState = MutableStateFlow(ReportTargetUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedWeek = MutableStateFlow(0)

    private val selectedWeek = _selectedWeek.asStateFlow()
        .onStart {
            val index = weeklyStatisticsManager.currentWeekIndex()
            _selectedWeek.value = index
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val topSummarySteps = selectedWeek.flatMapLatest{ selectedWeek ->
        val stepsInWeek = weeklyStatisticsManager.stepsInWeek.map {
            it[selectedWeek]
        }.map {
            val total = weeklyStatisticsManager.totalStepsInWeek(it)
            val average = weeklyStatisticsManager.averageStepsInWeek(it)
            TopSummaryUi(
                title = "Steps",
                value = total,
                subtitle = UiText.StringResourceText(R.string.daily_average,average)
            )

        }
        stepsInWeek
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val topSummaryCalories = selectedWeek.flatMapLatest { selectedWeek ->
        val caloriesBurned = weeklyStatisticsManager.caloriesBurned.map {
            it[selectedWeek]
        }.map {
            val total = weeklyStatisticsManager.caloriesBurnedTotal(it)
            val average = weeklyStatisticsManager.caloriesBurnedAverage(it)
            TopSummaryUi(
                title = "Calories",
                value = total.roundToInt(),
                subtitle = UiText.StringResourceText(R.string.daily_average,average)
            )
        }
        caloriesBurned
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val topSummaryUi = uiState.flatMapLatest{ uiState,  ->

        when(uiState.selectedTarget){
            ReportTarget.STEPS -> {
               topSummarySteps
            }
            ReportTarget.CALORIES -> {
                topSummaryCalories
            }
            ReportTarget.TIME ->{
                topSummarySteps
            }
            ReportTarget.DISTANCE -> {
                topSummarySteps
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TopSummaryUi())


    fun onAction(action: ReportTargetAction) {
        when(action){
            is ReportTargetAction.SetTargetAction -> {
                _uiState.update {
                    it.copy(selectedTarget = action.target)
                }
            }
        }
    }
}