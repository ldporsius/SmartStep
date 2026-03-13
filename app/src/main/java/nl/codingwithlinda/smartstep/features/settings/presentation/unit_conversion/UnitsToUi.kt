package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import nl.codingwithlinda.unit_conversion.data.lenght.LengthUnits
import nl.codingwithlinda.unit_conversion.data.weight.GRAM
import nl.codingwithlinda.unit_conversion.data.weight.KG
import nl.codingwithlinda.unit_conversion.data.weight.LBS
import nl.codingwithlinda.unit_conversion.data.weight.TestConverter
import nl.codingwithlinda.unit_conversion.data.weight.Weight
import nl.codingwithlinda.unit_conversion.data.weight.Weights
import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.core.domain.util.UiText.DynamicText

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