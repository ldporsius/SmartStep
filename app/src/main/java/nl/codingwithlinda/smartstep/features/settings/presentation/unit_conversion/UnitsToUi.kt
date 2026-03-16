package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import nl.codingwithlinda.unit_conversion.data.lenght.LengthUnits
import nl.codingwithlinda.unit_conversion.data.weight.Weight
import nl.codingwithlinda.unit_conversion.data.weight.Weights
import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.core.domain.util.UiText.DynamicText


//TODO replace with string resources or hardcoded values
fun Weight.toUi(): UiText{
    return when(this) {
        Weight.GRAM -> DynamicText("g")
        Weight.KG -> DynamicText("kg")
        Weight.LBS -> DynamicText("lbs")
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