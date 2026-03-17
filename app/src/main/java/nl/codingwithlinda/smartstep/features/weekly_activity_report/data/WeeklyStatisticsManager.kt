package nl.codingwithlinda.smartstep.features.weekly_activity_report.data

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
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
        }
            .map {
            it.average()
        }
    }

}
