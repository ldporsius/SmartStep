package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.data

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper
import java.time.ZonedDateTime

class AIUserMessages (
    private val dailyStepRepo: DailyStepRepo
){

    private fun today() = DateTimeHelper.toDateYYYYMMDD(System.currentTimeMillis())
    private val steps = dailyStepRepo.stepCountPlusUserOverride.map{ dailyStepCounts ->
        dailyStepCounts.firstOrNull(){
            it.dayEpochDay == today().dateEpochDay
        }?.stepCount ?: 0
    }
    private val goal = dailyStepRepo.getDailyStepGoals().map { goals ->
        goals.firstOrNull(){
            it.epochDay == today().dateEpochDay
        }?.goal ?: -1
    }

    val statisticsMessage = steps.combine(goal) {steps, goal ->

        if(goal == -1 || steps == -1){
            return@combine null
        }
        val zonedDateTime = ZonedDateTime.now()
        val timeOfDay = "${zonedDateTime.hour}:${zonedDateTime.minute}"

        val msg = """
                I am tracking my steps.
                My goal is $goal steps.
                I have made $steps steps.
                The time now is ${timeOfDay}
                Have I reached my goal?
                ${if(steps >= goal) "Yes" else "No"}
            """.trimIndent()

        AIMessage(
            message = msg,
            origin = AIMessageOrigin.USER
        )
    }
}