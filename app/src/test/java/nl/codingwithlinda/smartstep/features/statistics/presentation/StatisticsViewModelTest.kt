package nl.codingwithlinda.smartstep.features.statistics.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.data.walk_duration.WalkDurationRepoImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.tests.FakeUserSettingsRepo
import nl.codingwithlinda.smartstep.tests.FakeWalkDurationRepo
import nl.codingwithlinda.smartstep.tests.di.TestDispatcherProvider
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {

    private lateinit var statisticsViewModel: StatisticsViewModel
    private val userSettingsRepo = FakeUserSettingsRepo()
    val dailyStepRepo = FakeDailyStepRepo()
    val walkDurationRepo = WalkDurationRepoImpl()

    val testDispatcher = StandardTestDispatcher()
    val testDispatcherProvider = TestDispatcherProvider(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        statisticsViewModel = StatisticsViewModel(
            userSettingsRepo = userSettingsRepo,
            dailyStepRepo = dailyStepRepo,
            walkDurationRepo = walkDurationRepo,
            dispatcherProvider = testDispatcherProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Ignore
    @Test
    fun `test time update in StatisticsViewModel`() = runTest(testDispatcherProvider.testDispatcher) {
        val today = DailyStepCountCreator.toDateYYYYMMDD(System.currentTimeMillis().milliseconds.inWholeSeconds)
        walkDurationRepo.saveWalkDurationStart(
            WalkDurationStart(today.YYYY, today.MM, today.DD, System.currentTimeMillis())

        )
        val em0 = walkDurationRepo.sessions.first()
        assertThat(em0.size).isEqualTo(1)

        println("We have a session: ${em0}")

        //testing a flow won't work here???
    }

}