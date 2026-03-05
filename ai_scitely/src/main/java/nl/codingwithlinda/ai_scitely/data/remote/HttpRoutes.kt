package nl.codingwithlinda.ai_scitely.data.remote

object HttpRoutes {

    //private const val BASE_URL = "https://api.scitely.com/v1/"
    private const val BASE_URL = "https://api.openai.com/v1/"
    //const val CHAT_COMPLETIONS_CONSOLE = "https://console.scitely.com/v1/chat/completions"
    const val CHAT_COMPLETIONS_ENDPOINT = "${BASE_URL}chat/completions"
}