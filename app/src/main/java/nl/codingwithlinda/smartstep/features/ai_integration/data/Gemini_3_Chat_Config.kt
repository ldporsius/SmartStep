package nl.codingwithlinda.smartstep.features.ai_integration.data

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerationConfig
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.ThinkingConfig
import com.google.firebase.ai.type.ThinkingLevel
import java.util.Locale

open class Gemini_Chat_Config: GeminiGonfig {

    val systemInstruction = Content.Builder().setRole(
        "You are a fitness trainer assistant. " +
                "You encourage someone to reach their step count goal. "
    ).build()


    private val generationConfig = GenerationConfig.builder()
        .setThinkingConfig(ThinkingConfig.Builder()
            .setThinkingLevel(ThinkingLevel.LOW)
            .setIncludeThoughts(false)
            .build()
        )
        .build()

    override fun model(): GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = "gemini-3-flash",
            generationConfig = generationConfig,
            systemInstruction =  systemInstruction
        )
    private val promptInstructions = StringBuilder()
        .appendLine("You must generate one short textual message - a maximum of three sentences - that:")
        .appendLine("interprets the current activity state")
        .appendLine("does not contain medical advice")
        .appendLine("does not repeate any values of the users input")
        .appendLine("has a motivational or analytical tone")

    private val promptExamples = StringBuilder()
        .appendLine("A walk in a park or a run, depending on your age and physical condition.")
        .appendLine("Walk to the groceries store, don't drive.")
        .appendLine("Have you considered walking the dog of your elderly neighbour?")
        .toString()

    private val completePrompt = StringBuilder()
        .appendLine(promptInstructions.toString())
        .appendLine("Use the following as an inspiration to generate your response:")
        .appendLine(promptExamples)
        .appendLine("Don't judge. Offer practical solutions.")
        .appendLine("respond in the locale: ${Locale.getDefault()}.")
        .appendLine("Here is what the user says: ")

    override fun promptInstructions(): String {
        return completePrompt.toString()
    }
}