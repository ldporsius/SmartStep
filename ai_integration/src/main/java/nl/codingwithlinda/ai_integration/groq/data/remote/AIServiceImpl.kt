package nl.codingwithlinda.ai_integration.groq.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import nl.codingwithlinda.ai_integration.BuildConfig
import nl.codingwithlinda.ai_integration.groq.data.dto.AIRequest
import nl.codingwithlinda.ai_integration.groq.data.dto.AIResponse
import nl.codingwithlinda.ai_integration.groq.data.dto.response.GroqResponse
import nl.codingwithlinda.ai_integration.groq.domain.remote.AIService


class AIServiceImpl: AIService {
    private val httpClient: HttpClient = KtorClient.client()

    override suspend fun sendMessage(message: AIRequest): AIResponse {

        println("--- AI SERVICE --- SENDS MESSAGE TO Groq: $message")
        try {
            val result = httpClient.post(HttpRoutes.CHAT_COMPLETIONS_ENDPOINT) {
                this.contentType(ContentType.Application.Json)
                this.header("Authorization", "Bearer ${BuildConfig.GROQ_KEY}")
                this.setBody(message)

            }.body<GroqResponse>()


            return AIResponse(
                result.choices.firstOrNull()?.message?.content ?: "error"
            )
        }catch (e: Exception) {
            e.printStackTrace()
            return AIResponse(
                e.message ?: "error"
            )
        }
    }
}