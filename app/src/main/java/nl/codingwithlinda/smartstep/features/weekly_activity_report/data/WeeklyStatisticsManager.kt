package nl.codingwithlinda.smartstep.features.weekly_activity_report.data

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.repo.WalkDurationRepo
import nl.codingwithlinda.smartstep.core.domain.statistics.calculations.caloriesBurned
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.unit_conversion.data.weight.GramsWeight
import nl.codingwithlinda.unit_conversion.data.weight.WeightUnitConverter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class WeeklyStatisticsManager(
    private val userSettingsRepo: UserSettingsRepo,
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

    //////////////////////////////////////////////////////////////
    private val userHeightCm = userSettingsRepo.userSettingsObservable.map {
        it.heightCm
    }
    private val userWeightKG = userSettingsRepo.userSettingsObservable.map {
        it.weightGrams
    }.map {
        val grams = GramsWeight(it)
        WeightUnitConverter.toKg(grams)
    }
    private val gender = userSettingsRepo.userSettingsObservable.map {
        it.gender
    }

    val caloriesBurned = combine(stepsInWeek, userWeightKG, gender) { steps, weight, gender ->
        steps.map {
            it.map {
                caloriesBurned(it.stepCount, weight.weight, gender)
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
            weekdays.mapNotNull { date ->
                sessions.find {
                    it.start.dateYYYYMMDD.let {
                        (LocalDate.of(it.YYYY, it.MM, it.DD)) == date
                    }
                }
            }
        }.map {
            it.map {
                val timeDiff =(it.end?.timestamp ?: System.currentTimeMillis()) - (it.start.timestamp)
                val duration = timeDiff.milliseconds.inWholeMinutes
                duration
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

}
