package nl.codingwithlinda.ai_firebase.gemini.core

import com.google.firebase.ai.GenerativeModel

interface GeminiGonfig {
    fun promptInstructions(): String
}


interface GeminiFlashConfig: GeminiGonfig {
    fun model(): GenerativeModel
}