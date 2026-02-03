package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state

interface HeightSettingUiState {
    data class Imperial(val feet: Int, val inches: Int): HeightSettingUiState
    data class SI(val cm: Int): HeightSettingUiState
}