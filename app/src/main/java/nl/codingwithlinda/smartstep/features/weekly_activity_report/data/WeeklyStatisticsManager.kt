package nl.codingwithlinda.smartstep.features.weekly_activity_report.data

import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastSumBy
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserStatisticsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.statistics.calculations.calculateDistanceCm
import nl.codingwithlinda.smartstep.core.domain.statistics.calculations.caloriesBurned
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.WeeklyBreakdownStatus
import nl.codingwithlinda.unit_conversion.data.distance.ConcreteDistance
import nl.codingwithlinda.unit_conversion.data.distance.DistanceConverter
import nl.codingwithlinda.unit_conversion.data.weight.GramsWeight
import nl.codingwithlinda.unit_conversion.data.weight.WeightUnitConverter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class WeeklyStatisticsManager(
    private val userStatisticsRepo: UserStatisticsRepo,
    private val dailyStepRepo: DailyStepRepo,
    private val walkDurationRepo: WalkDurationRepo
) {
    val oldestDate = dailyStepRepo.stepCountPlusUserOverride.map {
        it.minByOrNull { it.dayEpochDay }?.dayEpochDay ?: LocalDate.now().toEpochDay()
    }

    val weeklyCalendar = oldestDate.map {oldest ->
        val start = LocalDate.ofEpochDay(oldest).minusWeeks(1).toEpochDay()
        val end = LocalDate.now().plusWeeks(1).toEpochDay()
        val range = start.rangeTo(end).toList()
            .map {
                LocalDate.ofEpochDay(it)
            }
        range
            .subList(
                range.indexOfFirst { it.dayOfWeek == DayOfWeek.MONDAY },
                range.indexOfLast { it.dayOfWeek == DayOfWeek.MONDAY}
            )
            .windowed(7, 7, false)
    }

    fun weekRangeAsString(range: List<LocalDate>): String {
        val m1 = range.first().month.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
        val d1 = range.first().dayOfMonth

        val m2 = range.last().month.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
        val d2 = range.last().dayOfMonth

        return "$m1 $d1 - $m2 $d2"
    }

    private fun today(): LocalDate = LocalDate.now()

    suspend fun currentWeekIndex(): Int =
        weeklyCalendar.first().indexOfFirst {
            it.contains(today())
        }

    ////////////////////////////////////////////////////////////////////
    val stepsInWeek= dailyStepRepo.stepCountPlusUserOverride.combine(weeklyCalendar){steps, weeks ->
        weeks.map { weekdays ->
            weekdays.map {date ->
                steps.find {
                    it.dayEpochDay == date.toEpochDay() } ?: DailyStepCountCreator.create(
                    date = date.toEpochDay(),
                    count = 0
                )
            }
        }
    }

    fun totalStepsInWeek(stepsInWeek: List<DailyStepCount>)=
        stepsInWeek.sumOf { it.stepCount }

    fun averageStepsInWeek(stepsInWeek: List<DailyStepCount>): Double {
        if (stepsInWeek.isEmpty()) return 0.0
        val result = stepsInWeek.map { it.stepCount }.average()
        if (result.isNaN()) return 0.0
        return result
    }

    suspend fun goalSteps(dayEpoch: Long): Int {
        return dailyStepRepo.getGoalForDay(DateTimeHelper.toDateYYYYMMDD(dayEpoch))?.goal ?: 0
    }
    //////////////////////////////////////////////////////////////

    val caloriesBurned = stepsInWeek.map { steps,  ->
        steps.map { dailyStepCounts ->
            dailyStepCounts.map {
                val settings = userStatisticsRepo.userSettingsForDay(it.dayEpochDay)
                val weightKg = WeightUnitConverter.toKg(GramsWeight(settings.weightGrams))
                it.dayEpochDay to caloriesBurned(it.stepCount, weightKg.weight, settings.gender)
            }
        }
    }

    fun caloriesBurnedTotal(calories: List<Double>) = calories.sum()

    fun caloriesBurnedAverage(calories: List<Double>): Double {
        if (calories.isEmpty()) return 0.0
        val result = calories.average()
        if (result.isNaN()) return 0.0
        return result
    }

    /////////////////////////////////////////////////////////////////////////
    val walkDuration = weeklyCalendar.combine(walkDurationRepo.sessions) { weeks, sessions ->
        weeks.map { weekdays ->
            weekdays.map { date ->
                val sessionsForDate = sessions.filter {
                    it.start.dateYYYYMMDD.dateEpochDay == date.toEpochDay()
                }.let{ walkSessions ->
                    walkSessions.filter { it.end != null }
                }.sumOf {
                    it.end!!.timestamp - it.start.timestamp
                }
                date to sessionsForDate.milliseconds.inWholeMinutes
            }
        }
    }

    fun totalWalkDuration(durations: List<Long>) = durations.sum()

    fun averageWalkDuration(durations: List<Long>): Double {
        if (durations.isEmpty()) return 0.0
        val result = durations.average()
        if (result.isNaN()) return 0.0
        return result
    }

    ////////////////////////////////////////////////////////////////////////////

    val distance = stepsInWeek.map { lists ->
        lists.map {
            it.map {step ->
                val settings = userStatisticsRepo.userSettingsForDay(step.dayEpochDay)
                val cm = calculateDistanceCm(personsHeightCm = settings.heightCm, stepsTaken = step.stepCount)
                step.dayEpochDay to DistanceConverter.toKm(ConcreteDistance.cm(cm))
            }
        }
    }

    fun totalDistance(distances: List<Double>) = distances.sum()

    fun averageDistance(distances: List<Double>): Double {
        if (distances.isEmpty()) return 0.0
        val result = distances.average()
        if (result.isNaN()) return 0.0
        return result
    }


    ///////////////////////////////////////////////////////////////
    suspend fun getStatus(dayEpoch: Long): WeeklyBreakdownStatus{
        val today = today().toEpochDay()
        val isBeforeAppInstall = dayEpoch < oldestDate.first()
        if (isBeforeAppInstall) return WeeklyBreakdownStatus.NOT_STARTED
        return when{
            dayEpoch > today -> {
                WeeklyBreakdownStatus.NOT_STARTED
            }
            dayEpoch == today -> {
                WeeklyBreakdownStatus.IN_PROGRESS
            }
            else -> {
                WeeklyBreakdownStatus.FINISHED
            }
        }
    }
}
