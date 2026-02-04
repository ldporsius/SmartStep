package nl.codingwithlinda.smartstep.core.domain.unit_conversion

sealed interface UnitSystemUnits {
    object IMPERIAL : UnitSystemUnits
    object SI : UnitSystemUnits
}


sealed interface LengthUnits{
    object CM : LengthUnits {
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.SI
    }

    object FEET_INCHES : LengthUnits {
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.IMPERIAL
    }

    val system: UnitSystemUnits
}

sealed interface WeightUnits {
    object KG: WeightUnits {
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.SI
    }

    object LBS: WeightUnits{
        override val system: UnitSystemUnits
            get() = UnitSystemUnits.IMPERIAL
    }

    val system: UnitSystemUnits
}