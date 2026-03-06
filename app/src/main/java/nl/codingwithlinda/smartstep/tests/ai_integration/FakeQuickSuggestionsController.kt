package nl.codingwithlinda.smartstep.tests.ai_integration

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.quick_suggestion.QuickSuggestionsController
import nl.codingwithlinda.smartstep.features.statistics.data.StatisticsManagerImpl
import nl.codingwithlinda.smartstep.tests.FakeActivityRecognitionRepo
import nl.codingwithlinda.smartstep.tests.FakeDailyStepRepo
import nl.codingwithlinda.smartstep.tests.FakeUserSettingsRepo
import nl.codingwithlinda.smartstep.tests.FakeWalkDurationRepo
import nl.codingwithlinda.smartstep.tests.di.TestDispatcherProvider


val fakeStatisticsManager =  StatisticsManagerImpl(
    userSettingsRepo = FakeUserSettingsRepo(),
    dailyStepRepo = FakeDailyStepRepo(){
        FakeActivityRecognitionRepo().getStepCountForDate(it)
    },
    walkDurationRepo = FakeWalkDurationRepo(),
    dispatcherProvider = TestDispatcherProvider(Dispatchers.Main),
    applicationScope = GlobalScope
)

val aiStateController = AIStateController(
    aiMessenger = FakeAIChatMessenger(
        dispatcherProvider = TestDispatcherProvider(Dispatchers.Main)
    ),
    aiSessionRepo = FakeAISessionRepo()
)
val fakeQuickSuggestionsController = QuickSuggestionsController(
    userSettingsRepo = FakeUserSettingsRepo(),
    statisticsManager = fakeStatisticsManager,
    aiStateController = aiStateController,
    dispatcherProvider = TestDispatcherProvider(Dispatchers.Main),
)