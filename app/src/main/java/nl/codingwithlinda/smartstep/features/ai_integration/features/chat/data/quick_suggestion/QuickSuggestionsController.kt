package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.quick_suggestion

import androidx.compose.ui.util.fastJoinToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.presentation.util.UiText
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.stepGoalRange
import nl.codingwithlinda.smartstep.core.domain.repo.UserSettingsRepo
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.intro_message.introMessage
import nl.codingwithlinda.smartstep.core.domain.statistics.StatisticsManager
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

class QuickSuggestionsController private constructor(
    private val userSettingsRepo: UserSettingsRepo,
    private val statisticsManager: StatisticsManager,
    private val aiStateController: AIStateController,
    private val dispatcherProvider: DispatcherProvider
) {

    companion object{
        @Volatile
        var instance: QuickSuggestionsController? = null

        @Synchronized
        fun getInstance(
            userSettingsRepo: UserSettingsRepo,
            statisticsManager: StatisticsManager,
            aiStateController: AIStateController,
            dispatcherProvider: DispatcherProvider
        ): QuickSuggestionsController{
            synchronized(this) {
                if (instance == null) {
                    instance = QuickSuggestionsController(
                        userSettingsRepo, statisticsManager, aiStateController, dispatcherProvider
                    )
                }
                return instance!!
            }
        }
    }
    private val _responses = MutableStateFlow<List<AIMessage>>(emptyList())
    val responses = _responses.asStateFlow()


    private suspend fun goal() = statisticsManager.todaysGoal.firstOrNull() ?: stepGoalRange.first()

    suspend fun activityProgress(): Int = statisticsManager.progressTowardsGoal.firstOrNull().let {
        it?.times(100)?.roundToInt() ?: 0
    }

    private fun today(): LocalDateTime = LocalDateTime.now()

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

    private suspend fun printTrend(): String{
        val pastWeek = List(7){
            today().minusDays(it.toLong())
        }.map{
            it.toLocalDate()
        }
        val trend = statisticsManager.trend.firstOrNull() ?: return ""
       val result = pastWeek.map {  day ->
           day to trend.filter {
               it.key.dateEpochDay == day.toEpochDay()
           }
        }.map {(localDate, values) ->
            val dayOfWeekShort = localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
           val percent = values.values.sum() * 100
           "$dayOfWeekShort: $percent%"
        }
        return result.fastJoinToString()

    }

    private fun handleResponse(result: Result<AIMessage, AIError>){
        when(result){
            is Result.Failure -> {
                _responses.value = _responses.value.plus(AIMessage(
                    message = "oops, something went wrong",
                    origin = AIMessageOrigin.ASSISTANT
                ))
            }
            is Result.Success -> {
                println("--- QUICK SUGGESTIONS CONTROLLER --- Message received: ${result.data}")
                _responses.update {
                    it.plus(result.data)
                }
            }
        }
    }

    suspend fun sendInitialMessage(){
        aiStateController.sendMessage(introMessage(activityProgress())).also {
            handleResponse(it)
        }
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
                aiStateController.sendMessage(msg).also {res ->
                   handleResponse(res)
                }
            }
        }
    )
    fun explainTodaysTrend() =  QuickSuggestion(
        title = UiText.DynamicText("Explain today\'s trend"),
        onAction = {
            CoroutineScope(dispatcherProvider.io).launch {
                val msg =   """
            Can you explain today\'s trend?
            The past week I achieved: ${printTrend()} percent of my goal.
            Today is ${today().dayOfWeek}
            In what direction am I going?.
        """.trimIndent()
                aiStateController.sendMessage(msg).also {
                    handleResponse(it)
                }
            }
        }
    )

    fun reachTodaysGoal() =  QuickSuggestion(
        title = UiText.DynamicText("How to reach today\'s goal?"),
        onAction = {
            CoroutineScope(dispatcherProvider.io).launch {
                val msg =   """
                    I am trying to make ${goal()} steps today.
                    Until now I have made ${statisticsManager.stepsToday.firstOrNull() ?: 0} steps.
            It is now ${today().hour} o'clock.
            How can I reach ${goal()} steps today?
            Please suggest some activity that fits my age ${getAge()} and gender ${getGender()}.
        """.trimIndent()
                aiStateController.sendMessage(msg).also {
                    handleResponse(it)
                }
            }
        }
    )


    val quickSuggestions = listOf<QuickSuggestion>(
        recommendWorkout(),
        explainTodaysTrend(),
        reachTodaysGoal()

    )

}