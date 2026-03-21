package nl.codingwithlinda.smartstep.features.main.statistics.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.CoroutineScope
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

        runBlocking {
            (1..10).onEach {
                val timestamp = System.currentTimeMillis()
                walkDurationRepo.saveWalkDurationStart(timestamp + 1.minutes.inWholeMilliseconds)
                walkDurationRepo.saveWalkDurationEnd(timestamp + 2.minutes.inWholeMilliseconds)
                delay(1.seconds)
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

        val total2 = mutableListOf<Long>()
        val job2 = backgroundScope.launch {
            weeklyManager.walkDuration.collect {
               it.onEach {
                   it.onEach {
                       println(it)
                       total2.add(it.second)
                   }
               }
            }
        }
        job2.join()
        job2.cancel()


        assertThat(total2.sum()).isEqualTo(10)

    }

}