package nl.codingwithlinda.smartstep.features.weekly_activity_report.data

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.statistics.calculations.caloriesBurned
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.unit_conversion.data.weight.GramsWeight
import nl.codingwithlinda.unit_conversion.data.weight.WeightUnitConverter
import java.time.DayOfWeek
import java.time.LocalDate

class WeeklyStatisticsManager(
    private val userSettingsRepo: UserSettingsRepo,
    private val dailyStepRepo: DailyStepRepo
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

    fun today() = LocalDate.now()

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

    fun averageStepsInWeek(stepsInWeek: List<DailyStepCount>)=
        stepsInWeek.map { it.stepCount }.average()

    val totalStepsInWeek = stepsInWeek.map{ stepsPerWeek ->
        stepsPerWeek.map{
            it.sumOf { it.stepCount }
        }
    }

    val averageStepsInWeek = stepsInWeek.map { steps ->
        steps.map { step ->
            step.map { it.stepCount }
        }.also{
            println("averages: $it")
        }.map {
            it.average()
        }
    }

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

    val caloriesBurned = combine(totalStepsInWeek, userWeightKG, gender) { steps, weight, gender ->
        steps.map {
            caloriesBurned(it, weight.weight, gender)
        }
    }


}
