package nl.codingwithlinda.smartstep.core.domain.unit_conversion


sealed interface UnitSystems {
    object IMPERIAL : UnitSystems
    object SI : UnitSystems
}


sealed interface LengthUnits{
    object CM : LengthUnits {
        override val system: UnitSystems
            get() = UnitSystems.SI
    }

    object FEET_INCHES : LengthUnits {
        override val system: UnitSystems
            get() = UnitSystems.IMPERIAL
    }

    val system: UnitSystems
}


