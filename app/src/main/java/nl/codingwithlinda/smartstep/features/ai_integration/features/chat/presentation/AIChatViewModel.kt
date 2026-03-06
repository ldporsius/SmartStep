package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.smartstep.core.domain.connectivity.ConnectivityMonitor
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.ai.domain.local_cache.AISessionRepo
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.AIChatAction
import nl.codingwithlinda.smartstep.features.ai_integration.features.chat.presentation.state.finite_state.AIChatState

class AIChatViewModel(
    private val aiStateController: AIStateController,
    private val aiSessionRepo: AISessionRepo,
    connectivityMonitor: ConnectivityMonitor
): ViewModel() {

    val _chats = MutableStateFlow<List<AIMessage>>(emptyList())
    val chats = _chats .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var message = ""
    private val _uiState = connectivityMonitor.isConnected.map { connected ->
        when(connected){
            true -> {
                AIChatState.Online(
                    message = message,
                    onAction = ::onAction
                )
            }
            false -> {
                AIChatState.Offline
            }
        }
    }
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AIChatState.Offline)

    fun onAction(action: AIChatAction){
        when(action){
            is AIChatAction.ChatInput -> {
                message = action.message
            }
            is AIChatAction.SendMessage -> {
                if(validateChatInput(action.message)){
                    sendMessage(action.message)
                }
            }
        }
    }

    private fun validateChatInput(input: String): Boolean{
        return input.length > 100
    }

    private fun sendMessage(msg: String){
        println("Sending message to chat: $msg")
        viewModelScope.launch {
            aiStateController.sendMessage(msg).let {res ->
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

}