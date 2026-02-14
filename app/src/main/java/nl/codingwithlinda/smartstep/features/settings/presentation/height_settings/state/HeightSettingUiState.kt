package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.HeightUnitConverter
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.maxHeightInches
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.minHeightFeet
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.minHeightInches

interface HeightSettingUiState {
    data class Imperial(override var valueCm: Int): HeightSettingUiState {

        val res = HeightUnitConverter.toImperial(valueCm.toDouble())

        val feet = res.first.coerceIn(minHeightFeet, maxHeightFeet)
        val inches = res.second.coerceIn(minHeightInches, maxHeightInches)


        override fun toUi() : UiText {
            return UiText.DynamicText("$feet ft $inches in")
        }
        override val system: UnitSystems
            get() = UnitSystems.IMPERIAL
    }

    data class SI(override var valueCm: Int): HeightSettingUiState {
        override fun toUi(): UiText = UiText.DynamicText("$valueCm cm")
        override val system: UnitSystems
            get() = UnitSystems.SI
    }

    var valueCm: Int

    fun toUi(): UiText

    val system: UnitSystems
}