package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import nl.codingwithlinda.ai.AIMessage
import nl.codingwithlinda.ai.AIMessageOrigin

sealed interface AIConnectivityUiState {

    data class OffLine(
        val message: AIMessage = AIMessage(
            message = "\uD83D\uDCE1 Connect to the internet to get AI insights",
            origin = AIMessageOrigin.ASSISTANT
        ),
        val onButtonClick: () -> Unit
    ): AIConnectivityUiState

    data class OnLine(
        val message: AIMessage?,
    ): AIConnectivityUiState


}