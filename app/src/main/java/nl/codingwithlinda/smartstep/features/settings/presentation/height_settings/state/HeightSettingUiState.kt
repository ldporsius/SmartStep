package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion.HeightUnitConverter

interface HeightSettingUiState {
    data class Imperial(override var valueCm: Int): HeightSettingUiState {

        val res = HeightUnitConverter().toImperial(valueCm.toDouble())

        val feet = res.first.toInt()
        val inches = res.second.toInt()


        override fun toUi() : UiText {
            return UiText.DynamicText("$feet ft $inches in")
        }
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.IMPERIAL
    }

    data class SI(override var valueCm: Int): HeightSettingUiState {
        override fun toUi(): UiText = UiText.DynamicText("$valueCm cm")
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.SI
    }

    var valueCm: Int

    fun toUi(): UiText

    val system: UnitSystemUnits
}