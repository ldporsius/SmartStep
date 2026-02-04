package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state

import nl.codingwithlinda.smartstep.core.domain.util.UiText

interface HeightSettingUiState {
    data class Imperial(val feet: Int, val inches: Int): HeightSettingUiState {
        override fun toUi() = UiText.DynamicText("$feet ft $inches in")
    }

    data class SI(val cm: Int): HeightSettingUiState {
        override fun toUi(): UiText = UiText.DynamicText("$cm cm")
    }

    fun toUi(): UiText

}