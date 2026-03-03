package nl.codingwithlinda.smartstep.features.ai_integration.data

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.GenerationConfig
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.ThinkingConfig
import com.google.firebase.ai.type.ThinkingLevel
import com.google.firebase.ai.type.generationConfig
import java.util.Locale

class Gemini_2_5_Chat_Config: Gemini_Chat_Config(){

    override fun model(): GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = "gemini-2.5-flash",
            systemInstruction =  systemInstruction
        )

}