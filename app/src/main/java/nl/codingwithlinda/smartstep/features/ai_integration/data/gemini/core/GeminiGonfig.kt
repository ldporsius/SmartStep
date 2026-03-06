package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core

import com.google.firebase.ai.GenerativeModel

interface GeminiGonfig {
    fun promptInstructions(): String
}


interface GeminiFlashConfig: GeminiGonfig {
    fun model(): GenerativeModel
}