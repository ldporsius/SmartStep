package nl.codingwithlinda.ai_scitely.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import nl.codingwithlinda.ai_scitely.BuildConfig

object KtorClient{
    private val client: HttpClient = HttpClient(CIO){
       /* install(Auth){
            this.bearer(){
                this.loadTokens {
                    BearerTokens("sk-scitely-${BuildConfig.SKITELY_AI_KEY}", null)
                }

            }
        }*/
           install(ContentNegotiation) {
               json()
           }
           install(Logging){
               level = LogLevel.ALL
           }
       }


    fun client() = client
}