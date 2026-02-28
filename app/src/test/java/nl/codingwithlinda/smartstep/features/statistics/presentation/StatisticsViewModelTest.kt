package nl.codingwithlinda.smartstep.features.statistics.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.data.walk_duration.WalkDurationRepoImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.features.statistics.data.StatisticsManagerImpl
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.tests.FakeUserSettingsRepo
import nl.codingwithlinda.smartstep.tests.di.TestDispatcherProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {

    private lateinit var statisticsViewModel: StatisticsViewModel
    private val userSettingsRepo = FakeUserSettingsRepo()
    val dailyStepRepo = FakeDailyStepRepo(){
        DailyStepCountCreator.create(1)
    }
    val walkDurationRepo = WalkDurationRepoImpl()

    val testDispatcher = UnconfinedTestDispatcher()
    val testDispatcherProvider = TestDispatcherProvider(testDispatcher)

    val statisticsManager = StatisticsManagerImpl(
        userSettingsRepo = userSettingsRepo,
        dailyStepRepo = dailyStepRepo,
        walkDurationRepo = walkDurationRepo,
        dispatcherProvider = testDispatcherProvider
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        statisticsViewModel = StatisticsViewModel(
            statisticsManager = statisticsManager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `test time update in StatisticsViewModel`() = runTest(testDispatcherProvider.testDispatcher) {
        val now = System.currentTimeMillis()
        val today = DailyStepCountCreator.create(1, now)

        walkDurationRepo.saveWalkDurationStart(
            WalkDurationStart(today.YYYY, today.MM, today.DD, now)
        )
        val sessions = walkDurationRepo.sessions.first()
        assertThat(sessions.size).isEqualTo(1)

        println("We have a session: ${sessions}, with start: ${sessions.first().start.dateString}")

        statisticsViewModel.statistics
        .test {

            //todo
            cancelAndConsumeRemainingEvents()

        }
    }

}