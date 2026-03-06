package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.KGWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.LBSWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.maxWeightPounds
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.minWeightPounds
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.weightRangeKg
import nl.codingwithlinda.core.domain.util.UiText
import kotlin.math.roundToInt

interface WeightSettingUiState {
    data class Imperial(private val lbsWeight: LBSWeight): WeightSettingUiState{

        val pounds = lbsWeight.weight
            .roundToInt()
            .coerceIn(minWeightPounds, maxWeightPounds)
        override val system: UnitSystems
            get() = UnitSystems.IMPERIAL

        override fun toUi(): UiText {
            return UiText.DynamicText("${pounds} lbs")
        }


    }
    data class SI(private val kg: KGWeight): WeightSettingUiState{
        val roundedKg = kg.weight
            .roundToInt()
            .coerceIn(weightRangeKg.first(), weightRangeKg.last())

        override val system: UnitSystems
            get() = UnitSystems.SI

        override fun toUi(): UiText {
            return UiText.DynamicText("${roundedKg} kg")
        }

    }

    val system: UnitSystems
    fun toUi(): UiText

}