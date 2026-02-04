package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.LengthUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.core.domain.util.UiText.DynamicText

fun WeightUnits.toUi(): UiText{
    return when(this) {
        WeightUnits.KG -> DynamicText("kg")
        WeightUnits.LBS -> DynamicText("lbs")
    }
}

fun LengthUnits.toUi(): UiText{
    return when(this) {
        LengthUnits.CM -> DynamicText("cm")
        LengthUnits.FEET_INCHES -> DynamicText("ft/in")
    }
}