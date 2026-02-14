package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.LengthUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.Weights
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.core.domain.util.UiText.DynamicText

fun WeightUnits.toUi(): UiText{
    return when(this) {
        is WeightUnits.Grams -> DynamicText("g")
        is WeightUnits.KG -> DynamicText("kg")
        is WeightUnits.LBS -> DynamicText("lbs")
    }
}

fun Weights.toUi(): UiText{
    return when(this) {
        Weights.GRAMS -> DynamicText("g")
        Weights.KG -> DynamicText("kg")
        Weights.LBS -> DynamicText("lbs")
    }
}

fun LengthUnits.toUi(): UiText{
    return when(this) {
        LengthUnits.CM -> DynamicText("cm")
        LengthUnits.FEET_INCHES -> DynamicText("ft/in")
    }
}