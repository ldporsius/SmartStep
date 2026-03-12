package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state

import nl.codingwithlinda.unit_conversion.domain.UnitSystems
import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.Length
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightInches
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.minHeightFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.minHeightInches

interface HeightSettingUiState {
    data class Imperial(var feetInches: Length.FeetInches): HeightSettingUiState {

        val feet = feetInches.feet.coerceIn(minHeightFeet, maxHeightFeet)
        val inches = feetInches.inches.coerceIn(minHeightInches, maxHeightInches)


        override fun toUi() : UiText {
            return UiText.DynamicText("$feet ft $inches in")
        }
        override val system: UnitSystems
            get() = UnitSystems.IMPERIAL
    }

    data class SI(var valueCm: Int): HeightSettingUiState {
        override fun toUi(): UiText = UiText.DynamicText("$valueCm cm")
        override val system: UnitSystems
            get() = UnitSystems.SI
    }


    fun toUi(): UiText

    val system: UnitSystems
}