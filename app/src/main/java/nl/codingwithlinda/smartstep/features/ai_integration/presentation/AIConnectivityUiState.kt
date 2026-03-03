package nl.codingwithlinda.smartstep.features.ai_integration.presentation

import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessage
import nl.codingwithlinda.smartstep.features.ai_integration.domain.AIMessageOrigin

sealed interface AIConnectivityUiState {

    data class OffLine(
        val message: AIMessage = AIMessage(
            message = "\uD83D\uDCE1 Connect to the internet to get AI insights",
            origin = AIMessageOrigin.ASSISTANT
        ),
        val onButtonClick: () -> Unit = {}
    ): AIConnectivityUiState

    data class OnLine(
        val message: AIMessage?,
    ): AIConnectivityUiState


}