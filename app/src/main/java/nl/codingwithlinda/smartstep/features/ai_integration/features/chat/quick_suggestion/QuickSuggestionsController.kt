package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.quick_suggestion

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.di.DispatcherProvider
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.settings.presentation.gender_settings.toUi
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import java.time.LocalDateTime

class QuickSuggestionsController(
    private val userSettingsRepo: UserSettingsRepo,
    private val statisticsManager: StatisticsManager,
    private val aiMessenger: AIMessenger,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun activityProgress() = statisticsManager.progressTowardsGoal.firstOrNull()
    fun today() = LocalDateTime.now()

    private suspend fun getAge(): Int{
        return 55
    }
    private suspend fun getGender(): String
    {
        userSettingsRepo.userSettingsObservable.firstOrNull()?.let {
            return it.gender.name
        }
        return "male"
    }
    fun recommendWorkout() =  QuickSuggestion(
        title = UiText.DynamicText("Recommend workout"),
        onAction = {
            CoroutineScope(dispatcherProvider.io).launch {
                val msg =   """
            Can you recommend a workout to achieve my daily step goal?
            My age is ${getAge()}. My gender is ${getGender()}.
            I have reached ${activityProgress()} percent of my goal.
            It is now ${today().hour} o'clock.
            Please suggest some activity that fits my age and gender.
        """.trimIndent()
                aiMessenger.chat(msg)
            }
        }
    )
    fun explainTodaysTrend() =  QuickSuggestion(
        title = UiText.DynamicText("Explain today\'s trend"),
        onAction = {
            CoroutineScope(dispatcherProvider.io).launch {
                val msg =   """
            Can you explain today\'s trend?
            The past week I achieved: ${statisticsManager.trend} percent of my goal.
            Today is ${today().dayOfWeek}
            In what direction am I going?.
        """.trimIndent()
                aiMessenger.chat(msg)
            }
        }
    )

    fun reachTodaysGoal() =  QuickSuggestion(
        title = UiText.DynamicText("How to reach today\'s goal?"),
        onAction = {
            CoroutineScope(dispatcherProvider.io).launch {
                val msg =   """
            It is now ${today().hour} o'clock.
            I have reached ${activityProgress()} percent of my goal.
            What can I still do today to reach my goal?
        """.trimIndent()
                aiMessenger.chat(msg)
            }
        }
    )


    val quickSuggestions = listOf<QuickSuggestion>(
        recommendWorkout(),
        explainTodaysTrend(),
        reachTodaysGoal()

    )

}