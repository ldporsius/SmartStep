package nl.codingwithlinda.smartstep.features.ai_integration.features.passive.presentation

import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin

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