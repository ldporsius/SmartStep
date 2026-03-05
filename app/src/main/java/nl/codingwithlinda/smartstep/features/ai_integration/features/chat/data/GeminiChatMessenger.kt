package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data

import android.util.Log.e
import com.google.firebase.ai.type.FirebaseAIException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.domain.repo.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.domain.util.SSResult
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AINormalState
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIResourceExhaustedState
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.GeminiFlashConfig
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIChatMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState

class GeminiChatMessenger(
    geminiGonfig: GeminiFlashConfig,
    private val aiSessionRepo: AISessionRepo

): AIChatMessenger(geminiGonfig) {
    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }

    private val normalState = AINormalState(this, aiSessionRepo)
    private val exhaustedState = AIResourceExhaustedState(this, aiSessionRepo)
    private var state: AIState = normalState
    private val messageHistory = mutableListOf<AIMessage>()
    private val _messages = MutableStateFlow<List<AIMessage>>(emptyList())


    override suspend fun chat(text: String) {
        val msg = create(text)

        messageHistory.add(
            msg
        )
        _messages.update {
            messageHistory.toList()
        }

        val result = state.sendMessage(msg)
        when(result){
            is Result.Failure -> {
                when(result.error){
                    FireBaseAIError.OtherError -> {
                        //todo
                    }
                    is FireBaseAIError.ResourceExhausted -> {
                        state = exhaustedState
                        _messages.update {
                            it.plus(
                                AIMessage(
                                    "Not able to respond",
                                    AIMessageOrigin.ASSISTANT
                                )
                            )
                        }
                    }
                }
            }
            is Result.Success -> {
                messageHistory.add(
                    result.data
                )
                _messages.update {
                    messageHistory.toList()
                }
            }
        }
    }

    override suspend fun send(message: AIMessage): SSResult<AIMessage, FireBaseAIError> {
        return super.send(message)
    }

    override val messages: Flow<List<AIMessage>> = _messages
}