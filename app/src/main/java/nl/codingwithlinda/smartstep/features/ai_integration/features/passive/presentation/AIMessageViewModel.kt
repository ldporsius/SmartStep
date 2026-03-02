package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.data.connectivity.ConnectivityMonitor
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.presentation.AIConnectivityUiState

class AIMessageViewModel(
    private val aiMessenger: AIMessenger,
    private val connectivityMonitor: ConnectivityMonitor
): ViewModel() {

    val emptyMessage = AIMessage(
        message = "empty message",
        origin = AIMessageOrigin.ASSISTANT
    )

    val isLoading = MutableStateFlow(false)

    init {
        connectivityMonitor.isConnected.onEach { isConnected ->

        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }

    val testMessageSteps = AIMessage(
        message = "I am tracking my steps. My daily goal is 10000 steps. I have set 200 so far. It is early in the morning.",
        origin = AIMessageOrigin.USER
    )
    val latestMessage = aiMessenger.messages.map {
        it.firstOrNull()
    }.onStart {
        aiMessenger.send(
            testMessageSteps
        ).let {res ->
            isLoading.value = false
            when(res){
                is Result.Failure -> {
                    println("failure ${res.error.message}")
                }
                is Result.Success -> {
                    println("success ${res.data.message}")
                }
            }

        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    val uiState = connectivityMonitor.isConnected.combine(aiMessenger.messages){ connected, messages ->
        when(connected){
            true -> {
                AIConnectivityUiState.OnLine(
                    message = messages.firstOrNull(),
                    onButtonClick = {
                        //navtodetail
                    }
                )
            }
            false -> {
                AIConnectivityUiState.OffLine(
                    onButtonClick = {
                        viewModelScope.launch {
                            aiMessenger.send(testMessageSteps)
                        }
                    }
                )
            }
        }
    }.onStart {
        aiMessenger.send(
            testMessageSteps
        ).let { res ->
            isLoading.value = false
            when (res) {
                is Result.Failure -> {
                    println("failure ${res.error.message}")
                }

                is Result.Success -> {
                    println("success ${res.data.message}")
                }
            }

        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AIConnectivityUiState.OnLine(message = null))
}