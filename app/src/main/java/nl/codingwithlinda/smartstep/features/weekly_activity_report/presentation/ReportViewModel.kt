package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.presentation.util.UiText
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.domain.statistics.calculations.caloriesBurned
import nl.codingwithlinda.smartstep.features.weekly_activity_report.data.WeeklyStatisticsManager
import nl.codingwithlinda.smartstep.features.weekly_activity_report.domain.ReportTarget
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetUiState
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.WeekPickerAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.WeekPickerUiState
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

    val weekPickerUiState = selectedWeek.map { index ->
        WeekPickerUiState(
            isPreviousEnabled = index > 0,
            isNextEnabled = index < weeklyStatisticsManager.currentWeekIndex(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeekPickerUiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val topSummarySteps = selectedWeek.flatMapLatest{ selectedWeek ->
        val stepsInWeek = weeklyStatisticsManager.stepsInWeek.map {
            it[selectedWeek]
        }.map {
            val total = weeklyStatisticsManager.totalStepsInWeek(it)
            val average = weeklyStatisticsManager.averageStepsInWeek(it)
            TopSummaryUi(
                title = UiText.StringResourceText(R.string.steps),
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
            val total =try {
                weeklyStatisticsManager.caloriesBurnedTotal(it).roundToInt()
            }catch (e: Exception) {
                e.printStackTrace()
            0
            }

            val average = weeklyStatisticsManager.caloriesBurnedAverage(it)
            TopSummaryUi(
                title = UiText.StringResourceText(R.string.calories),
                value = total,
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


    fun onTargetAction(action: ReportTargetAction) {
        when(action){
            is ReportTargetAction.SetTargetAction -> {
                _uiState.update {
                    it.copy(selectedTarget = action.target)
                }
            }
        }
    }

    fun onWeekAction(action: WeekPickerAction) {

        when(action){
            WeekPickerAction.PreviousWeek -> {
                _selectedWeek.update {
                    (it - 1).coerceAtLeast(0)
                }
            }
            WeekPickerAction.NextWeek -> {
                viewModelScope.launch {
                    _selectedWeek.update {
                        (it + 1).coerceAtMost(weeklyStatisticsManager.currentWeekIndex())
                    }
                }
            }

        }
    }

}
