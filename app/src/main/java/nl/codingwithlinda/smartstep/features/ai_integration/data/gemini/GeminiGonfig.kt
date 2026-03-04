package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini

import com.google.firebase.ai.GenerativeModel

interface GeminiGonfig {

    fun model(): GenerativeModel
    fun promptInstructions(): String
}