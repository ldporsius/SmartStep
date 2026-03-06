package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.passive

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerativeBackend
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiFlashConfig
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.models.GeminiModels
import java.util.Locale

class Gemini_2_5_Config: GeminiFlashConfig {

    private val systemInstruction = Content.Builder().setRole(
        "You are a fitness trainer assistant. " +
                "You encourage someone to reach their step count goal. " +
                "You never use more then one sentence."

    ).build()

    override fun model(): GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = GeminiModels.FLASH2.modelName,
            systemInstruction =  systemInstruction
        )

    private val promptInstructions = StringBuilder()
        .appendLine("You must generate one short textual message - ONE SENTENCE ONLY - that:")
        .appendLine("interprets the current activity state")
        .appendLine("does not contain medical advice")
        .appendLine("does not repeate any values of the users input")
        .appendLine("has a motivational or analytical tone")

    private val promptExamples = StringBuilder()
        .appendLine("You’re on track today. Keep the pace steady.")
        .appendLine("You’re a bit behind your goal — a short walk could help.")
        .appendLine("Great job! You’ve already reached today’s goal.")
        .toString()

    private val completePrompt = StringBuilder()
        .appendLine(promptInstructions.toString())
        .appendLine("Use the following as a guide to generate your response:")
        .appendLine(promptExamples)
        .appendLine("Be encouraging and enthusiast.")
        .appendLine("respond in the locale: ${Locale.getDefault()}.")
        .appendLine("Here is what the user says: ")

    override fun promptInstructions(): String {
        return completePrompt.toString()
    }
}