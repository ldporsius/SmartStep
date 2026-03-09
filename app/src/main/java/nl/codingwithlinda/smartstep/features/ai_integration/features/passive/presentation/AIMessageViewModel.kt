package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import nl.codingwithlinda.smartstep.core.domain.connectivity.ConnectivityMonitor
import nl.codingwithlinda.core.domain.util.Result
import nl.codingwithlinda.smartstep.features.ai_integration.data.finite_state.AIStateController
import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin
import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.smartstep.features.ai_integration.features.passive.data.AIUserMessages
import nl.codingwithlinda.smartstep.features.ai_integration.presentation.error.toUIString
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

@OptIn(FlowPreview::class)
class AIMessageViewModel(
    private val aiStateController: AIStateController,
    connectivityMonitor: ConnectivityMonitor,
    statisticsManager: StatisticsManager,
    aiUserMessages: AIUserMessages
): ViewModel() {


    private val goal = statisticsManager.todaysGoal
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), -1)

    private val statisticsMessage = aiUserMessages.statisticsMessage

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
                                aiStateController.sendMessage(msg)
                            }
                        }
                    }
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AIConnectivityUiState.OnLine(message = null))

    init {
        viewModelScope.launch {
        statisticsManager.progressTowardsGoal.collectLatest {
            if(it < 0) return@collectLatest
            delay(500)
            makeRequest()
        }
        }
    }


    fun makeRequest() = viewModelScope.launch {
            val msg = statisticsMessage.firstOrNull()?: return@launch
            yield()

            val result = aiStateController.sendMessage(msg)
            when(result){
                is Result.Failure -> {
                    when(result.error){
                        is AIError.ResourceExhausted -> {
                            result.data?.let { msg ->
                                _response.update {
                                    msg
                                }
                            }
                        }
                        AIError.OtherError -> {
                            _response.update {
                                AIMessage(
                                    message = result.error.toUIString(),
                                    origin = AIMessageOrigin.ASSISTANT
                                )
                            }
                        }
                    }
                }
                is Result.Success -> {
                    _response.update {
                        result.data
                    }
                }
            }
        }
}