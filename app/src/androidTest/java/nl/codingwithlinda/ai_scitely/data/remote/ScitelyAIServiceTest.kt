package nl.codingwithlinda.ai_scitely.data.remote

import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.ai_scitely.data.dto.AIRequest
import org.junit.Test

class ScitelyAIServiceTest {


    val service = ScitelyAIService()

    @Test
    fun testScitelyChat() = runBlocking{
        val response = service.sendMessage(AIRequest("Hello"))

        println(response)

    }


}