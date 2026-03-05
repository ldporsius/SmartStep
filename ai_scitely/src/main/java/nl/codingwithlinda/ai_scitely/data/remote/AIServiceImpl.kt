package nl.codingwithlinda.ai_scitely.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.KotlinxSerializationJsonExtensionProvider
import kotlinx.serialization.json.Json
import nl.codingwithlinda.ai_scitely.BuildConfig
import nl.codingwithlinda.ai_scitely.data.dto.AIRequest
import nl.codingwithlinda.ai_scitely.data.dto.AIResponse


class AIServiceImpl(

): AIService {
    private val httpClient: HttpClient = KtorClient.client()

    private val chatbody = """
     {
      "model": "meta-llama/llama-4-scout-17b-16e-instruct",
      "messages": [
        { "role": "user", "content": "Hallo!" }
      ]
    }
    """.trimIndent()
    override suspend fun sendMessage(message: AIRequest): AIResponse? {

        val json = Json.encodeToString(message)
        println("--- AI SERVICE --- SENDS MESSAGE json: $json")
        try {
            val result = httpClient.post(HttpRoutes.CHAT_COMPLETIONS_ENDPOINT) {
                this.contentType(ContentType.Application.Json)
                this.header("Authorization", "Bearer ${BuildConfig.GROQ_KEY}")
                this.setBody(message)

            }
            return AIResponse(
                result.body()
            )
        }catch (e: Exception) {
            e.printStackTrace()
            return AIResponse(
                e.message ?: "error"
            )
        }
    }
}