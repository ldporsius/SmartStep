package nl.codingwithlinda.smartstep.features.weekly_activity_report.data

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import nl.codingwithlinda.smartstep.util.BaseStepRepoTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class WeeklyStatisticsManagerTest : BaseStepRepoTest() {

    val userSettingsRepo = mockk<UserSettingsRepo>(relaxed = true)
    val dailyStepRepo = fakeDailyStepRepo

    val manager = WeeklyStatisticsManager(
        userSettingsRepo = userSettingsRepo,
        dailyStepRepo = dailyStepRepo
    )

    @Test
    fun `test oldest day - the oldest day from the daily steps valid`() = runTest {
        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(1).toEpochDay()),
            stepCount = 100
        )
        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(0).toEpochDay()),
            stepCount = 100
        )
        manager.oldestDate.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(LocalDate.now().minusWeeks(1).toEpochDay())

        }
    }

    @Test
    fun `test weekly calendar - one week past`() = runTest{

        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(1).toEpochDay()),
            stepCount = 100
        )
        manager.weeklyCalendar.test {
            val item = awaitItem()

            item.onEach {list ->
                list.onEach {
                    println("${it.dayOfWeek} $it")
                }
            }

            val firstDay = item.first().first()
            assertThat(firstDay.dayOfWeek).isEqualTo(DayOfWeek.MONDAY)
            val lastday = item.first().last()
            assertThat(lastday.dayOfWeek).isEqualTo(DayOfWeek.SUNDAY)

            assertEquals(2, item.size)

            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `test weekly calendar - no weeks past`() = runTest{

        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(0).toEpochDay()),
            stepCount = 100
        )
        manager.weeklyCalendar.test {
            val item = awaitItem()

            item.onEach {list ->
                list.onEach {
                    println("${it.dayOfWeek} $it")
                }
            }

            val firstDay = item.first().first()
            assertThat(firstDay.dayOfWeek).isEqualTo(DayOfWeek.MONDAY)
            val lastday = item.first().last()
            assertThat(lastday.dayOfWeek).isEqualTo(DayOfWeek.SUNDAY)

            assertEquals(1, item.size)

            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `test weekly steps - weeks with no steps are included`() = runTest{

        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(2).toEpochDay()),
            stepCount = 100
        )

        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(0).toEpochDay()),
            stepCount = 200
        )
        manager.stepsInWeek.test {
            val item = awaitItem()

            item.onEach {list ->
                list.onEach {
                    println("${it}")
                }
            }

            assertThat(item.flatten().size).isEqualTo(3 * 7)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test weekly statistics - sum steps correct`() = runTest {
        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(10).toEpochDay()),
            stepCount = 100
        )
        manager.totalStepsInWeek.test {
            val item = awaitItem()

            assertThat(item.first()).isEqualTo(100)

            assertThat(item.last()).isEqualTo(0)
        }
    }

    @Test
    fun `test weekly statistics - average steps correct`() = runTest {
        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(1).toEpochDay()),
            stepCount = 70
        )
        dailyStepRepo.saveDailyStepCountUserOverride(
            DateTimeHelper.toDateYYYYMMDD(LocalDate.now().minusWeeks(0).toEpochDay()),
            stepCount = 1
        )
        manager.averageStepsInWeek.test {
            val item = awaitItem()

            assertThat(item.size).isEqualTo(2)

            assertThat(item.first()).isEqualTo(10.0)

            assertThat(item.last()).isEqualTo(1.0/7)
        }
    }
}