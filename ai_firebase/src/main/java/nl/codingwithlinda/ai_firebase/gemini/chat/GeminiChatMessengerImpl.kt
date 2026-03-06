package nl.codingwithlinda.ai_firebase.gemini.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.ai_firebase.gemini.core.GeminiFlashConfig
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.core.domain.util.Result

class GeminiChatMessengerImpl(
    geminiGonfig: GeminiFlashConfig,
    dispatcherProvider: DispatcherProvider

): GeminiAIChatMessenger(geminiGonfig, dispatcherProvider) {
    override fun create(text: String): AIMessage {
        return AIMessage(
            message = text,
            origin = AIMessageOrigin.USER
        )
    }

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

        val result = send(msg)
        when(result){
            is Result.Failure-> {
                when(result.error){
                    AIError.OtherError -> {
                        //todo
                    }
                    is AIError.ResourceExhausted -> {
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
            is Result.Success-> {
                messageHistory.add(
                    result.data
                )
                _messages.update {
                    messageHistory.toList()
                }
            }
        }
    }

    override suspend fun send(message: AIMessage): Result<AIMessage, AIError> {
        return super.send(message)
    }

    override val messages: Flow<List<AIMessage>> = _messages
}