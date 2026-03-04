package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import nl.codingwithlinda.smartstep.core.domain.connectivity.ConnectivityMonitor
import nl.codingwithlinda.smartstep.core.domain.repo.AISessionRepo
import nl.codingwithlinda.smartstep.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AINormalState
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIResourceExhaustedState
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessenger
import nl.codingwithlinda.smartstep.features.ai_integration.domain.finite_state.AIState
import nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation.AIConnectivityUiState
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

@OptIn(FlowPreview::class)
class AIMessageViewModel(
    private val aiMessenger: AIMessenger,
    private val aiSessionRepo: AISessionRepo,
    connectivityMonitor: ConnectivityMonitor,
    statisticsManager: StatisticsManager
): ViewModel() {

    private val aiNormalState = AINormalState(
        aiMessenger = aiMessenger,
        aiSessionRepo = aiSessionRepo
    )
    private val aiResourceExhaustedState = AIResourceExhaustedState(
        aiMessenger = aiMessenger,
        aiSessionRepo = aiSessionRepo
    )

    private val aiState = MutableStateFlow<AIState>(aiNormalState)

    private val goal = statisticsManager.todaysGoal
    private val statisticsMessage = statisticsManager.stepsToday.combine(goal) {steps, goal ->

        val zonedDateTime = ZonedDateTime.now()
        val timeOfDay = zonedDateTime.hour
        AIMessage(
            message = "I am tracking my steps. My progress is ${steps.toFloat()/goal}. It is ${timeOfDay} o'clock.",
            origin = AIMessageOrigin.USER
        )
    }

    private val _response = MutableStateFlow<AIMessage?>(null)

    val uiState = connectivityMonitor.isConnected.combine(_response){ connected, message ->
        when(connected){
            true -> {
                AIConnectivityUiState.OnLine(
                    message = message,
                )
            }
            false -> {
                AIConnectivityUiState.OffLine(
                    onButtonClick = {
                        viewModelScope.launch {
                            statisticsMessage.lastOrNull()?.let { msg ->
                                aiMessenger.send(msg)
                            }
                        }
                    }
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AIConnectivityUiState.OnLine(message = null))

    init {
        goal
            .debounce(5.minutes)
            .onEach {
                makeRequest()
            }.launchIn(viewModelScope)
    }


    fun makeRequest()=viewModelScope.launch {
            val msg = statisticsMessage.firstOrNull()?: return@launch
            yield()

            val result = aiState.value.sendMessage(msg)
            when(result){
                is Result.Failure -> {
                    aiState.update {
                        aiResourceExhaustedState
                    }
                    _response.update {
                        AIMessage(
                            message = "Something went wrong",
                            origin = AIMessageOrigin.ASSISTANT
                        )
                    }
                }
                is Result.Success -> {
                    aiState.update {
                        aiNormalState
                    }
                    _response.update {
                        result.data
                    }
                }
            }
        }
}