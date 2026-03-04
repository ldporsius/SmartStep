package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini

import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.LiveGenerativeModel
import com.google.firebase.ai.type.PublicPreviewAPI

interface GeminiGonfig {
    fun promptInstructions(): String
}

interface GeminiLiveConfig: GeminiGonfig {
    @OptIn(PublicPreviewAPI::class)
    fun model(): LiveGenerativeModel
}

interface GeminiFlashConfig: GeminiGonfig {
    fun model(): GenerativeModel
}