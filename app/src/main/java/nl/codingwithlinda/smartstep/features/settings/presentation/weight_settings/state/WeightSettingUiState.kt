package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.WeightUnitConverter
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.maxWeightPounds
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.minWeightPounds

interface WeightSettingUiState {
    data class Imperial(val kg: Int,): WeightSettingUiState{
        val converter = WeightUnitConverter
        val pounds = converter.toImperial(kg.toDouble()).coerceIn(minWeightPounds, maxWeightPounds)
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.IMPERIAL

        override fun toUi(): UiText {
            return UiText.DynamicText("${pounds} lbs")
        }

    }
    data class SI(val kg: Int): WeightSettingUiState{
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.SI

        override fun toUi(): UiText {
            return UiText.DynamicText("$kg kg")
        }

    }

    val system: UnitSystemUnits
    fun toUi(): UiText

}