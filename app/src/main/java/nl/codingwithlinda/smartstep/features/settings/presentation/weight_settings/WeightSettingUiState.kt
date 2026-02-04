package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings

interface WeightSettingUiState {
    data class Imperial(val pounds: Int,): WeightSettingUiState
    data class SI(val kg: Int): WeightSettingUiState

}