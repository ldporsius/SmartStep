package nl.codingwithlinda.ai_scitely.data.remote

import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.ai_scitely.data.dto.AIRequest
import nl.codingwithlinda.ai_scitely.data.dto.Message
import org.junit.Test
import kotlin.String

class AIServiceImplTest {
    val service = AIServiceImpl()

    @Test
    fun testScitelyChat() = runBlocking{
        val request = AIRequest(
            messages = listOf(
                Message(
                    role = "user",
                    content = "write a story about a unicorn in one sentence"
                )
            )
        )


        val response = service.sendMessage(request)

        println(response)

    }


}