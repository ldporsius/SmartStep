package nl.codingwithlinda.ai_scitely.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import nl.codingwithlinda.ai_scitely.BuildConfig
import nl.codingwithlinda.ai_scitely.data.dto.AIRequest
import nl.codingwithlinda.ai_scitely.data.dto.AIResponse


class ScitelyAIService(

): AIService {
    private val httpClient: HttpClient = KtorClient.client()

    private val chatbody = """
       {
        "model": "gpt-5.2",
        "input": "Write a one-sentence bedtime story about a unicorn."
    }
    """.trimIndent()
    override suspend fun sendMessage(message: AIRequest): AIResponse? {
        println("--- SCITELY AI SERVICE --- SENDS MESSAGE")
        try {
            val result = httpClient.post(HttpRoutes.CHAT_COMPLETIONS_ENDPOINT) {
                this.contentType(ContentType.Application.Json)
                this.header("Authorization", "Bearer ${BuildConfig.OPEN_AI_KEY}")
                this.setBody(chatbody)

            }
            return AIResponse(
                result.bodyAsText()
            )
        }catch (e: Exception) {
            e.printStackTrace()
            return AIResponse(
                e.message ?: "error"
            )
        }
    }
}