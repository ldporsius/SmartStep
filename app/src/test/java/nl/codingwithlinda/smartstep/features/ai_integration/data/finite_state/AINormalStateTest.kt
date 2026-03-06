package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.ai.data.finite_state.AINormalState
import nl.codingwithlinda.ai_firebase.tests.FakeAIMessenger
import nl.codingwithlinda.ai_firebase.tests.FakeAISessionRepo
import nl.codingwithlinda.smartstep.tests.di.TestDispatcherProvider
import nl.codingwithlinda.smartstep.util.BaseJunitTest
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