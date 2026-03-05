package nl.codingwithlinda.ai_scitely.data.remote

object HttpRoutes {

    //const val CHAT_COMPLETIONS_CONSOLE = "https://console.scitely.com/v1/chat/completions"

    //private const val BASE_URL = "https://api.scitely.com/v1/"
    //private const val BASE_URL = "https://api.openai.com/v1/"
    private const val BASE_URL = "https://api.groq.com/openai/v1/"

    const val CHAT_COMPLETIONS_ENDPOINT = "${BASE_URL}chat/completions"
}