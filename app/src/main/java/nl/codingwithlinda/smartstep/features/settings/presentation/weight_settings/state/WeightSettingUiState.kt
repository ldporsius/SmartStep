package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.WeightUnitConverter
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.maxWeightPounds
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.minWeightPounds
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import kotlin.math.roundToInt

interface WeightSettingUiState {
    data class Imperial(val kg: Double): WeightSettingUiState{
        val converter = WeightUnitConverter
        val pounds = converter.convert(kg, UnitSystemUnits.SI, WeightUnits.LBS)
            .roundToInt()
            .coerceIn(minWeightPounds, maxWeightPounds)
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.IMPERIAL

        override fun toUi(): UiText {
            return UiText.DynamicText("${pounds} lbs")
        }
        init {
            println("--- WeightSettingUiState imperial --- kg == $kg, pounds == $pounds")
        }

    }
    data class SI(val kg: Int): WeightSettingUiState{
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.SI

        override fun toUi(): UiText {
            return UiText.DynamicText("${kg} kg")
        }
        val roundedKg = kg

    }

    val system: UnitSystemUnits
    fun toUi(): UiText

}