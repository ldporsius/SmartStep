package nl.codingwithlinda.smartstep.features.statistics.presentation

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.core.data.repo.walk_duration.WalkDurationRepoImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkDurationStart
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.features.main.statistics.data.DailyStatisticsManager
import nl.codingwithlinda.smartstep.FakeUserSettingsRepo
import nl.codingwithlinda.smartstep.FakeWalkDurationRepo
import nl.codingwithlinda.smartstep.di.TestDispatcherProvider
import nl.codingwithlinda.smartstep.features.main.statistics.presentation.StatisticsViewModel
import nl.codingwithlinda.smartstep.util.BaseStepRepoTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest: BaseStepRepoTest() {

    private lateinit var statisticsViewModel: StatisticsViewModel
    private val userSettingsRepo = FakeUserSettingsRepo()

    val walkDurationRepo = FakeWalkDurationRepo()

    val testDispatcherProvider = TestDispatcherProvider(testDispatcher)

    val statisticsManager = DailyStatisticsManager(
        userSettingsRepo = userSettingsRepo,
        dailyStepRepo = fakeDailyStepRepo,
        walkDurationRepo = walkDurationRepo,
        dispatcherProvider = testDispatcherProvider,
        applicationScope = CoroutineScope(testDispatcher)
    )

    @Before
    override fun setup() {
       super.setup()
        statisticsViewModel = StatisticsViewModel(
            statisticsManager = statisticsManager
        )
    }

    @Test
    fun `test time update in StatisticsViewModel`() = runTest(testDispatcherProvider.testDispatcher) {
        val now = System.currentTimeMillis()

        walkDurationRepo.saveWalkDurationStart(
            WalkDurationStart(now)
        )
        val sessions = walkDurationRepo.sessions.first()
        assertThat(sessions.size).isEqualTo(1)

        println("We have a session: ${sessions}, with start: ${sessions.first().start.dateString}")
    }

}