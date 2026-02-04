package nl.codingwithlinda.smartstep.features.settings.presentation.common

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.LengthUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits

interface SelectableUnitsUi<U> {
    val units: List<U>
    var currentSelection: Int
}


class SelectableUnitsLengthUi: SelectableUnitsUi<LengthUnits> {
    override val units: List<LengthUnits>
        get() = listOf<LengthUnits>(
            LengthUnits.CM,
            LengthUnits.FEET_INCHES
        )
    override var currentSelection: Int = 0
}

class SelectableUnitsWeightUi: SelectableUnitsUi<WeightUnits> {
    override val units: List<WeightUnits>
        get() = listOf(
            WeightUnits.KG,
            WeightUnits.LBS
        )
    override var currentSelection: Int = 0
}