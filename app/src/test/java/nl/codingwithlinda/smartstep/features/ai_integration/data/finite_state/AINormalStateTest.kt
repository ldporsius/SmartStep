package nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state

import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin
import nl.codingwithlinda.ai.data.finite_state.AINormalState
import nl.codingwithlinda.ai_firebase.tests.FakeAIMessenger
import nl.codingwithlinda.ai_firebase.tests.FakeAISessionRepo
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.smartstep.tests.di.TestDispatcherProvider
import nl.codingwithlinda.smartstep.util.BaseStepRepoTest
import org.junit.Test

class AINormalStateTest : BaseStepRepoTest(){

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

        assertThat(result).isInstanceOf(Result.Failure::class)

        with(result as Result.Failure){
            assertThat(error).isInstanceOf(nl.codingwithlinda.ai.domain.error.AIError.ResourceExhausted::class)
            assertThat(data).isNotNull()

        }

    }
}