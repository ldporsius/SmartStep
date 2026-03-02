package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
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
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.milliseconds

class AIMessageViewModel(
    private val aiMessenger: AIMessenger,
    private val connectivityMonitor: ConnectivityMonitor,
    private val statisticsManager: StatisticsManager
): ViewModel() {

    val emptyMessage = AIMessage(
        message = "Please introduce yourself",
        origin = AIMessageOrigin.USER
    )

    val isLoading = MutableStateFlow(false)

    init {
        connectivityMonitor.isConnected.onEach { isConnected ->

        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }


    val aiMessage = statisticsManager.progressTowardsGoal.map {
        val zonedDateTime = ZonedDateTime.now()
        val timeOfDay = zonedDateTime.hour
        AIMessage(
            message = "I am tracking my steps. My progress is $it. It is ${timeOfDay} o'clock.",
            origin = AIMessageOrigin.USER
        )
    }


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
                            aiMessage.lastOrNull()?.let {msg ->
                                aiMessenger.send(msg)
                            }
                        }
                    }
                )
            }
        }
    }.onStart {

        val msg = aiMessage.firstOrNull()?: emptyMessage
        aiMessenger.send(
            msg
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AIConnectivityUiState.OnLine(message = null))
}