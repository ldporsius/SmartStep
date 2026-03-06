package nl.codingwithlinda.ai_integration.groq.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient{
    private val client: HttpClient = HttpClient(Android){
       /* install(Auth){
            this.bearer(){
                this.loadTokens {
                    BearerTokens("sk-scitely-${BuildConfig.SKITELY_AI_KEY}", null)
                }

            }
        }*/
           install(ContentNegotiation) {
               json(Json {
                   ignoreUnknownKeys = true
                   encodeDefaults = true
               })
           }
           install(Logging){
               level = LogLevel.ALL
           }
       }


    fun client() = client
}