package nl.codingwithlinda.smartstep.features.weekly_average.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.util.BaseStepRepoTest
import org.junit.Test

class WeeklyAverageViewModelTest : BaseStepRepoTest(){


    val vm = WeeklyAverageViewModel(fakeDailyStepRepo)

    @Test
    fun `test past week - all seven days in range`()= runTest(testDispatcher) {
        vm.pastWeek.onEach {
            println("past day: $it")
        }
    }

    @Test
    fun `test weekly average viewmodel - flow of items`() = runTest {
        vm.lastSevenStepCounts.test {
            val em0 = awaitItem()
            println("em0: $em0")
            val em1 = awaitItem()
            println("em1: $em1")
            assertThat(em1).hasSize(7)
        }
    }


}