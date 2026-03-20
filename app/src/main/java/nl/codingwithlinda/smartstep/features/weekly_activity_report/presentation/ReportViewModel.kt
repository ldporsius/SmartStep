package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.presentation.util.UiText
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.weekly_activity_report.data.WeeklyStatisticsManager
import nl.codingwithlinda.smartstep.features.weekly_activity_report.domain.ReportTarget
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetUiState
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.WeekPickerAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.WeekPickerUiState
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.TopSummaryUi
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.WeeklyBreakdownStatus
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.WeeklyBreakdownUi
import nl.codingwithlinda.unit_conversion.data.distance.DistanceConverter
import nl.codingwithlinda.unit_conversion.domain.UnitSystems
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.emptyList
import kotlin.math.roundToInt

class ReportViewModel(
    private val weeklyStatisticsManager: WeeklyStatisticsManager,
    private val userSettingsRepo: UserSettingsRepo
): ViewModel() {

    private val _targetUiState = MutableStateFlow(ReportTargetUiState())
    val targetUiState = _targetUiState.asStateFlow()

    private val _selectedWeek = MutableStateFlow(0)

    private val selectedWeek = _selectedWeek.asStateFlow()

    init {
        viewModelScope.launch {
            val index = weeklyStatisticsManager.currentWeekIndex()
            _selectedWeek.value = index
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val weekPickerUiState = selectedWeek.flatMapLatest{ index ->
        val weekRange = weeklyStatisticsManager.weeklyCalendar.map {
            weeklyStatisticsManager.weekRangeAsString(it[index])
        }.map {weekRange ->
            WeekPickerUiState(
                isPreviousEnabled = index > 0,
                weekRange = weekRange,
                isNextEnabled = index < weeklyStatisticsManager.currentWeekIndex(),
            )
        }
        weekRange
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeekPickerUiState())

    ///////////////////////////////////////////////////////////////////////////////////////
    @OptIn(ExperimentalCoroutinesApi::class)
    private val topSummarySteps = selectedWeek.flatMapLatest{ selectedWeek ->
        val stepsInWeek = weeklyStatisticsManager.stepsInWeek.map {
            it[selectedWeek]
        }.map {
            val total = weeklyStatisticsManager.totalStepsInWeek(it)
            val average = weeklyStatisticsManager.averageStepsInWeek(it)
            TopSummaryUi(
                title = UiText.StringResourceText(R.string.steps),
                value = UiText.DynamicText(total.toString()),
                subtitle = UiText.StringResourceText(R.string.daily_average,average)
            )

        }
        stepsInWeek
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val topSummaryCalories = selectedWeek.flatMapLatest { selectedWeek ->
        val caloriesBurned = weeklyStatisticsManager.caloriesBurned.map {
            it[selectedWeek]
        }.map { calories ->
            val calList = calories.map { (dayEpoch, calories) ->
                calories
            }
            val total =try {
                weeklyStatisticsManager.caloriesBurnedTotal(calList).roundToInt()
            }catch (e: Exception) {
                e.printStackTrace()
                0
            }

            val average = weeklyStatisticsManager.caloriesBurnedAverage(calList)
            TopSummaryUi(
                title = UiText.StringResourceText(R.string.calories),
                value = UiText.DynamicText(total.toString()),
                subtitle = UiText.StringResourceText(R.string.daily_average,average)
            )
        }
        caloriesBurned
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val topSummaryWalkDuration = selectedWeek.flatMapLatest { selectedWeek ->
        val walkDuration = weeklyStatisticsManager.walkDuration.map {
            it[selectedWeek]
        }.map {
            val durations = it.map { (_, duration) ->
                duration
            }
            val total = weeklyStatisticsManager.totalWalkDuration(durations).toInt()
            val average = weeklyStatisticsManager.averageWalkDuration(durations)
            TopSummaryUi(
                title = UiText.StringResourceText(R.string.walk_duration),
                value = UiText.DynamicText(total.toString()),
                subtitle = UiText.StringResourceText(R.string.daily_average, average)
            )
        }

        walkDuration
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val topSummaryDistance = selectedWeek.flatMapLatest { selectedWeek ->
        val system = userSettingsRepo.unitSystemObservable.first()
        val title = system.let {
            when (it) {
                UnitSystems.IMPERIAL -> {
                    UiText.StringResourceText(R.string.miles)
                }
                UnitSystems.SI -> {
                    UiText.StringResourceText(R.string.kilometers)
                }
            }
        }

        val distance = weeklyDistance(selectedWeek).map { distances ->
            val values = distances.map {(dayEpoch, distance) ->
              distance.value
            }

            val total = weeklyStatisticsManager.totalDistance(values)
            val average = weeklyStatisticsManager.averageDistance(values)

            TopSummaryUi(
                title = title,
                value = UiText.StringResourceText(R.string.distance, total),
                subtitle = UiText.StringResourceText(R.string.daily_average, average)
            )

        }
        distance
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val topSummaryUi = targetUiState.flatMapLatest{ uiState,  ->

        when(uiState.selectedTarget){
            ReportTarget.STEPS -> {
                topSummarySteps
            }
            ReportTarget.CALORIES -> {
                topSummaryCalories
            }
            ReportTarget.TIME ->{
                topSummaryWalkDuration
            }
            ReportTarget.DISTANCE -> {
                topSummaryDistance
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TopSummaryUi())

/////////////////////////////////////////////////////////////////////////////////////////////////
    @OptIn(ExperimentalCoroutinesApi::class)
    val weekItems = selectedWeek.combine(targetUiState){ selectedWeek , target ->
       when(target.selectedTarget){
           ReportTarget.STEPS -> {
               weeklyStepsBreakdown(selectedWeek)
           }
           ReportTarget.CALORIES -> {
               weeklyCaloriesBreakdown(selectedWeek)
           }
           ReportTarget.TIME -> {
              weeklyWalkDurationBreakdown(selectedWeek)
           }
           ReportTarget.DISTANCE -> {
               weeklyDistanceBreakdown(selectedWeek)
           }
       }
    }.flatMapLatest {
        it
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

///////////////////////////////////////////////////////////////////////////////////////////////////
    private suspend fun labelText(strRes: Int, dayEpoch: Long, callback: () -> Int): UiText {
        val status = weeklyStatisticsManager.getStatus(dayEpoch)
        return when(status){
            WeeklyBreakdownStatus.FINISHED -> UiText.StringResourceText(strRes, callback())
            WeeklyBreakdownStatus.IN_PROGRESS -> UiText.StringResourceText(strRes, callback())
            WeeklyBreakdownStatus.NOT_STARTED -> UiText.DynamicText("No data")
        }
    }
    private suspend fun labelText(dayEpoch: Long): UiText{
        val status = weeklyStatisticsManager.getStatus(dayEpoch)
        return when(status){
            WeeklyBreakdownStatus.FINISHED -> UiText.DynamicText("")
            WeeklyBreakdownStatus.IN_PROGRESS -> UiText.DynamicText("")
            WeeklyBreakdownStatus.NOT_STARTED -> UiText.DynamicText("No data")
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    private fun weeklyStepsBreakdown(weekIndex: Int) =  weeklyStatisticsManager.stepsInWeek.map {
        it[weekIndex]
    }.map {
        it.map {

            val goal = weeklyStatisticsManager.goalSteps(it.dayEpochDay)
            val status = weeklyStatisticsManager.getStatus(it.dayEpochDay)
            val labelText = labelText(R.string.goal, it.dayEpochDay){
                goal
            }
            WeeklyBreakdownUi(
                dayName = displayWeekName(it.dayEpochDay),
                value = UiText.DynamicText(it.stepCount.toString()),
                unit = UiText.StringResourceText(R.string.steps),
                status = status,
                label = labelText
            )
        }
    }

    private fun weeklyDistanceBreakdown(weekIndex: Int) = weeklyDistance(weekIndex).map {
        it.map {(dayEpoch, distance) ->

            val system = userSettingsRepo.unitSystemObservable.first()
            val unitText = when(system){
                UnitSystems.IMPERIAL -> {
                    UiText.StringResourceText(R.string.miles)
                }
                UnitSystems.SI -> {
                    UiText.StringResourceText(R.string.kilometers)
                }
            }
            WeeklyBreakdownUi(
                dayName = displayWeekName(dayEpoch),
                value = UiText.StringResourceText(R.string.distance, distance.value),
                unit = unitText,
                status = weeklyStatisticsManager.getStatus(dayEpoch),
                label = labelText(dayEpoch)
            )
        }
    }
    private fun weeklyCaloriesBreakdown(weekIndex: Int) = weeklyStatisticsManager.caloriesBurned.map {
        it[weekIndex]
    }.map {
        it.map {(dayEpoch, calories) ->
            WeeklyBreakdownUi(
                dayName = displayWeekName(dayEpoch),
                value = UiText.DynamicText(calories.roundToInt().toString()),
                unit = UiText.StringResourceText(R.string.calories),
                status = weeklyStatisticsManager.getStatus(dayEpoch),
                label = labelText(dayEpoch)
            )
        }
    }

    private fun weeklyWalkDurationBreakdown(weekIndex: Int) = weeklyStatisticsManager.walkDuration.map {
        it[weekIndex]
    }.map {
        it.map { (localDate, duration) ->
            WeeklyBreakdownUi(
                dayName = displayWeekName(localDate.toEpochDay()),
                value = UiText.DynamicText(duration.toString()),
                unit = UiText.StringResourceText(R.string.walk_duration),
                status = weeklyStatisticsManager.getStatus(localDate.toEpochDay()),
                label = labelText(localDate.toEpochDay())
                )
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////
    private fun weeklyDistance(weekIndex: Int) = weeklyStatisticsManager.distance.map {
        it[weekIndex]
    }.map {distancesKm ->
        val system = userSettingsRepo.unitSystemObservable.first()
        val distance = system
            .let{system ->
                when(system){
                    UnitSystems.IMPERIAL -> {
                        distancesKm.map {
                            it.first to DistanceConverter.toMile(it.second)
                        }
                    }
                    UnitSystems.SI -> {
                        distancesKm
                    }
                }
            }
        distance
    }

    ///////////////////////////////////////////////////////////////////
    private fun displayWeekName(dayEpoch: Long) = LocalDate.ofEpochDay(dayEpoch).dayOfWeek.getDisplayName(
        TextStyle.FULL_STANDALONE, Locale.getDefault()
    )
    ///////////////////////////////////////////////////////////////////
    fun onTargetAction(action: ReportTargetAction) {
        when(action){
            is ReportTargetAction.SetTargetAction -> {
                _targetUiState.update {
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
