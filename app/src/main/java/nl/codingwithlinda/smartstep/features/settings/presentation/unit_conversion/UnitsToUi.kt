package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.LengthUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.GRAM
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.KG
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.LBS
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.TestConverter
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.Weight
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.Weights
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.core.domain.util.UiText.DynamicText

fun Weight.toUi(): UiText{
    return when(this) {
        GRAM -> DynamicText("g")
        KG -> DynamicText("kg")
        LBS -> DynamicText("lbs")
        is TestConverter -> DynamicText("test")
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