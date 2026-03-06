package nl.codingwithlinda.ai_integration.groq.data.remote

object HttpRoutes {

    private const val BASE_URL = "https://api.groq.com/openai/v1/"

    const val CHAT_COMPLETIONS_ENDPOINT = "${BASE_URL}chat/completions"
}