package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini

import com.google.firebase.ai.type.PublicPreviewAPI
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger

@OptIn(PublicPreviewAPI::class)
class GeminiAIMessenger(
    var geminiGonfig: GeminiFlashConfig
): AIMessenger(geminiGonfig) {

  /*  override suspend fun send(message: AIMessage): SSResult<AIMessage, FireBaseAIError> {

        return withContext(Dispatchers.IO){
            try {
                val prompt = geminiGonfig.promptInstructions() + message.message

                val response = geminiGonfig.model().generateContent(prompt)
                println("--- GEMINI AI MESSENGER -- response: ${response.text}")
                val msg =  AIMessage(
                    message = response.text ?: "no comment from AI",
                    origin = AIMessageOrigin.ASSISTANT
                )
                Result.Success(msg)
            }catch (e: FirebaseAIException){
                e.printStackTrace()

                if (e.message?.contains("RESOURCE_EXHAUSTED") == true){
                    val retryInSeconds = e.message?.let {
                        it.substringAfterLast("Please retry in ").substringBefore("s")
                            .toDoubleOrNull() ?: 0.0
                    } ?: 0.0
                    println("retryInSeconds: $retryInSeconds")

                    val retryAtTime = System.currentTimeMillis().milliseconds.inWholeSeconds + (retryInSeconds).toLong()

                    System.currentTimeMillis().milliseconds.inWholeSeconds
                    return@withContext Result.Failure(FireBaseAIError.ResourceExhausted(retryAtTime))
                }
                Result.Failure(FireBaseAIError.OtherError)
            }
        }
    }
  */
}