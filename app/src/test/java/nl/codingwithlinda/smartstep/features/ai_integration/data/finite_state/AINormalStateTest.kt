package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.passive.Gemini_2_5_Config
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.tests.ai_integration.FakeAIMessenger
import nl.codingwithlinda.smartstep.tests.ai_integration.FakeAISessionRepo
import nl.codingwithlinda.smartstep.tests.di.TestDispatcherProvider
import nl.codingwithlinda.smartstep.util.BaseJunitTest
import org.junit.Assert.*
import org.junit.Test

class AINormalStateTest : BaseJunitTest(){

    private val aiMessenger = FakeAIMessenger(
        dispatcherProvider = TestDispatcherProvider(testDispatcher)
    )
    private val aiSessionRepo = FakeAISessionRepo()
    val aiNormalState = AINormalState(
        aiMessenger = aiMessenger,
        aiSessionRepo = aiSessionRepo
    )

    @Test
    fun `test AI normal state`() = runTest{
        val result = aiNormalState.sendMessage(
            AIMessage(
                message = "test",
                origin = AIMessageOrigin.USER
            )
        )

        println("result: $result")
    }
}