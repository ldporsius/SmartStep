package nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.application.di.DispatcherProvider
import nl.codingwithlinda.smartstep.features.ai_integration.domain.local_cache.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.AIError
import nl.codingwithlinda.smartstep.core.domain.util.FireBaseAIError
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.domain.util.SSResult
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AINormalState
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIResourceExhaustedState
import nl.codingwithlinda.smartstep.features.ai_integration.data.gemini.core.GeminiFlashConfig
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState
import kotlin.collections.plus

class GeminiChatMessengerImpl(
    geminiGonfig: GeminiFlashConfig,
    aiSessionRepo: AISessionRepo,
    dispatcherProvider: DispatcherProvider

): GeminiAIChatMessenger(geminiGonfig, dispatcherProvider) {
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
                    AIError.OtherError -> {
                        //todo
                    }
                    is AIError.ResourceExhausted -> {
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

    override suspend fun send(message: AIMessage): SSResult<AIMessage, AIError> {
        return super.send(message)
    }

    override val messages: Flow<List<AIMessage>> = _messages
}