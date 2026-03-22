package nl.codingwithlinda.smartstep.features.main.statistics.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.FakeUserSettingsRepo
import nl.codingwithlinda.smartstep.FakeUserStatisticsRepo
import nl.codingwithlinda.smartstep.FakeWalkDurationRepo
import nl.codingwithlinda.smartstep.di.TestDispatcherProvider
import nl.codingwithlinda.smartstep.features.weekly_activity_report.data.WeeklyStatisticsManager
import nl.codingwithlinda.smartstep.util.BaseStepRepoTest
import org.junit.Test
import java.time.LocalDate
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DailyStatisticsManagerTest: BaseStepRepoTest() {

    val walkDurationRepo = FakeWalkDurationRepo()
    val userSettingsRepo = FakeUserSettingsRepo()
    val dailyManager = DailyStatisticsManager(
        userSettingsRepo = userSettingsRepo,
        dailyStepRepo = fakeDailyStepRepo,
        walkDurationRepo = walkDurationRepo,
        dispatcherProvider = TestDispatcherProvider(testDispatcher),
        applicationScope = CoroutineScope(testDispatcher)
    )

    val weeklyManager = WeeklyStatisticsManager(
        userStatisticsRepo = FakeUserStatisticsRepo(),
        dailyStepRepo = fakeDailyStepRepo,
        walkDurationRepo = walkDurationRepo
    )

    @Test
    fun testWalkDuration_sumMinutesCorrect()= runTest(testDispatcher){
        val timestamp = System.currentTimeMillis()
        val yesterday = timestamp.minus(1.days.inWholeMilliseconds)

        runBlocking {

            (1..10).onEach {

                //yesterday
                walkDurationRepo.saveWalkDurationStart(yesterday + it.minutes.inWholeMilliseconds)
                walkDurationRepo.saveWalkDurationEnd(yesterday + it.minutes.inWholeMilliseconds + 1.minutes.inWholeMilliseconds)

                walkDurationRepo.saveWalkDurationStart(timestamp + it.minutes.inWholeMilliseconds)
                walkDurationRepo.saveWalkDurationEnd(timestamp + it.minutes.inWholeMilliseconds + 1.minutes.inWholeMilliseconds)

            }
        }

        val s = walkDurationRepo.sessions.toList()
        s.onEach {
            it.onEach {
                println("${it.start.timestamp.milliseconds.inWholeMinutes} - ${it.end?.timestamp?.milliseconds?.inWholeMinutes}")
            }
            println("${it.map {
                ((it.end?.timestamp ?: System.currentTimeMillis()) - it.start.timestamp).milliseconds.inWholeMinutes
            }}")
        }
        val total = mutableListOf<Int>()
        val job = backgroundScope.launch {
            dailyManager.timeWalked.onEach {
                println("time: $it")
                total.add(it)
            }.collect()
        }

        job.join()

        total.onEach {
            println("total: $it")
        }

        assertThat(total.sum()).isEqualTo(10)

        val total2 = mutableListOf<Map<Long, Long>>()
        val job2 = backgroundScope.launch {
            weeklyManager.walkDuration.collect {
                it.onEach {
                    val day = it.map {
                        it.first.toEpochDay() to it.second
                    }.toMap()
                   total2.add(day)
                }
            }
        }

        job2.cancelAndJoin()

        println("_".repeat(50))

        total2.onEach {
            println(it)
        }
        val yesterdayTotal = total2.filter {
            it.keys.any {
                it == yesterday
            }
        }
        assertThat(yesterdayTotal).isEqualTo(10)

        return@runTest
    }

}