package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.smartstep.core.domain.connectivity.ConnectivityMonitor
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.quick_suggestion.QuickSuggestionsController
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatAction
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.finite_state.AIChatState

class AIChatViewModel(
    private val aiStateController: AIStateController,
    private val quickSuggestionsController: QuickSuggestionsController,
    connectivityMonitor: ConnectivityMonitor,
    private val dispatcherProvider: DispatcherProvider
): ViewModel() {


    private val _chats = MutableStateFlow<List<AIMessage>>(emptyList())
    val chats = _chats
        .onStart {
            if(_chats.value.isEmpty()){
               sendIntro()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var message = MutableStateFlow("")
    private val _uiState = connectivityMonitor.isConnected.combine(message) { connected , msg ->
        when(connected){
            true -> {
                AIChatState.Online(
                    message = msg,
                    onAction = ::onAction
                )
            }
            false -> {
                AIChatState.Offline
            }
        }
    }
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AIChatState.Offline)

    init {
            quickSuggestionsController.responses.onEach {responses ->
                println("--- AI CHAT VIEW MODEL --- RESPONSE FROM GROQ: ${responses}")
                _chats.update {
                    responses
                }
            }.launchIn(viewModelScope)

    }
    fun onAction(action: AIChatAction){
        when(action){
            AIChatAction.Intro ->{

            }
            is AIChatAction.ChatInput -> {
                message.update {
                    action.message
                }
            }
            is AIChatAction.SendMessage -> {
                if(validateChatInput(action.message)){
                    sendMessage(action.message)
                }
                message.update {
                    ""
                }
            }
        }
    }

    private fun validateChatInput(input: String): Boolean{
        return input.length > 10
    }

    private fun sendMessage(msg: String){
        println("Sending message to chat: $msg")
        viewModelScope.launch {
            _chats.update {
                it.plus(
                    AIMessage(
                        message = msg,
                        origin = AIMessageOrigin.USER
                    )
                )
            }
            aiStateController.sendMessage(msg).let {res ->
                println("--- AI CHAT VIEWMODEL --- Message sent: $res")
                when(res){
                    is Result.Failure -> {
                        _chats.update {
                            it.plus(AIMessage(
                                message = "oops",
                                origin = AIMessageOrigin.ASSISTANT
                            ))
                        }
                    }
                    is Result.Success -> {
                        _chats.update {
                            it.plus(res.data)
                        }
                    }
                }
            }
        }
    }

    private fun sendIntro(){
        viewModelScope.launch {
            quickSuggestionsController.sendInitialMessage()
        }
    }

}