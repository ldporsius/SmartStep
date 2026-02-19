package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.GramsWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.LBS
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.LBSWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.convertWeight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.maxWeightPounds
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.minWeightPounds
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.weightRangeKg
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import kotlin.math.roundToInt

interface WeightSettingUiState {
    data class Imperial(private val grams: Double): WeightSettingUiState{

        val pounds = GramsWeight(grams).let {
            convertWeight(it, LBS).weight
        }
            .roundToInt()
            .coerceIn(minWeightPounds, maxWeightPounds)
        override val system: UnitSystems
            get() = UnitSystems.IMPERIAL

        override fun toUi(): UiText {
            return UiText.DynamicText("${pounds} lbs")
        }
        init {
            println("--- WeightSettingUiState imperial --- ,grams = $grams, pounds == $pounds")
        }

    }
    data class SI(private val grams: Double): WeightSettingUiState{
        val roundedKg = grams.div(1000.0)
            .roundToInt()
            .coerceIn(weightRangeKg.first(), weightRangeKg.last())

        override val system: UnitSystems
            get() = UnitSystems.SI

        override fun toUi(): UiText {
            return UiText.DynamicText("${roundedKg} kg")
        }

        init {
            println("--- WeightSettingUiState SI --- grams == $grams, roundedKg == $roundedKg")
        }
    }

    val system: UnitSystems
    fun toUi(): UiText

}