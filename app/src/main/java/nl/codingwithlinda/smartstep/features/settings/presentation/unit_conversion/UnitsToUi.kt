package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.domain.util.UiText

fun UnitSystemUnits.toUi(): UiText{
    return when(this){
        UnitSystemUnits.CM -> UiText.DynamicText("cm")
        UnitSystemUnits.FEET_INCHES -> UiText.DynamicText("ft/in")
    }
}