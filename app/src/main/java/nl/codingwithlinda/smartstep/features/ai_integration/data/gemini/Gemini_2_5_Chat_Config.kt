package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.models.GeminiModels

class Gemini_2_5_Chat_Config: Gemini_Chat_Config(){

    override fun model(): GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = GeminiModels.FLASH2.modelName,
            systemInstruction =  systemInstruction
        )

}